package org.smartparam.engine.core.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.cache.MapParamCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.SmartMatcherRepository;
import org.smartparam.engine.core.repository.SmartTypeRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.loader.ParamRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.function.Function;

/**
 * Klasa dostarcza przygotowane parametry na podstawie nazwy. Wykorzystuje
 * cache, poniewaz przygotowanie parametru jest kosztowne.
 * <p>
 *
 * Przygotowanie parametru sklada sie z 3 glownych krokow:
 * <ol>
 * <li> wczytanie parametru przy pomocy <i>loadera</i> (np. z bazy danych),
 * <li> zamiana struktury Parameter/ParameterEntry na blizniacza strukture
 * PreparedParameter/PreparedEntry,
 * <li> zbudowanie indeksu wyszukiwania dla wczytanego parametru.
 * </ol>
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public class SmartParamPreparer extends AbstractAnnotationScanner implements ParamPreparer {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    /**
     * Dostep do systemu typow silnika.
     */
    private TypeRepository typeProvider = null;

    /**
     * Dostep do systemu matcherow.
     */
    private MatcherRepository matcherProvider = null;

    /**
     * Loader parametrow.
     */
    private ParamRepository paramRepository;

    private FunctionProvider functionProvider;

    /**
     * Cache.
     */
    private ParamCache cache;

    @PostConstruct
    public void initializeProviders() {
        if (matcherProvider == null) {
            SmartMatcherRepository smartMatcherProvider = new SmartMatcherRepository();
            smartMatcherProvider.setScannerProperties(getScannerProperties());

            smartMatcherProvider.scan();
            matcherProvider = smartMatcherProvider;
        }

        if (typeProvider == null) {
            SmartTypeRepository smartTypeProvider = new SmartTypeRepository();
            smartTypeProvider.setScannerProperties(getScannerProperties());

            smartTypeProvider.scan();
            typeProvider = smartTypeProvider;
        }

        if (cache == null) {
            cache = new MapParamCache();
        }
    }

    @Override
    public PreparedParameter getPreparedParameter(String paramName) {

        PreparedParameter pp = cache.get(paramName);

        if (pp == null) {
            Parameter p = paramRepository.load(paramName);

            if (p == null) {
                logger.warn("param not found: {}", paramName);
                return null;
            }

            pp = prepare(p);
            cache.put(paramName, pp);
        }

        return pp;
    }

    /**
     * Buduje przygotowany (skompilowany) parametr na podstawie wczytanego przez
     * <tt>loader</tt> parametru <tt>p</tt>.
     *
     * @param p parametr wczytany przez loader
     *
     * @return skompilowany parametr
     */
    private PreparedParameter prepare(Parameter p) {

        /*
         * przygotowanie podstawowej konfiguracji parametru
         */
        PreparedParameter pp = new PreparedParameter();
        pp.setName(p.getName());
        pp.setMultivalue(p.isMultivalue());
        pp.setInputLevelsCount(p.getInputLevels());
        pp.setNullable(p.isNullable());
        pp.setCacheable(p.isCacheable());
        pp.setArray(p.isArray());
        pp.setArraySeparator(p.getArraySeparator());

        // typ parametru jest wymagany dla parametrow single-value (czyli standardowych)
        Type<?> paramType = typeProvider.getType(p.getType());
        pp.setType(paramType);

        if (paramType == null && !p.isMultivalue()) {
            throw new SmartParamDefinitionException(
                    SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                    "Parameter " + p.getName() + " has undefined param type: " + p.getType());
        }

        /*
         * przygotowanie konfiguracji poziomow (typy, matchery)
         */

        int levelCount = p.getLevelCount();
        PreparedLevel[] levels = new PreparedLevel[levelCount];
        Type<?>[] types = new Type<?>[levelCount];
        Matcher[] matchers = new Matcher[levelCount];

        for (int i = 0; i < levelCount; i++) {
            Level lev = p.getLevel(i);

            Type<?> type = null;
            if (lev.getType() != null) {
                type = typeProvider.getType(lev.getType());

                if (type == null) {
                    throw new SmartParamDefinitionException(
                            SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                            "Parameter " + p.getName() + ": level(" + (i + 1) + ") has unknown type: " + lev.getType());
                }
            }

            Matcher matcher = null;
            if (lev.getMatcherCode() != null) {
                matcher = matcherProvider.getMatcher(lev.getMatcherCode());

                if (matcher == null) {
                    throw new SmartParamDefinitionException(
                            SmartParamErrorCode.UNKNOWN_MATCHER,
                            "Parameter " + p.getName() + ": level(" + (i + 1) + ") has unknown matcher: " + lev.getMatcherCode());
                }
            }

            Function levelCreator = null;
            if(lev.getLevelCreator() != null) {
                    levelCreator = functionProvider.getFunction(lev.getLevelCreator());
            }
            levels[i] = new PreparedLevel(type, lev.isArray(), matcher, levelCreator);
            types[i] = type;
            matchers[i] = matcher;
        }

        pp.setLevels(levels);

        /*
         * zbudowanie indeksu wyszukiwania
         */
        if (p.isCacheable()) {
            buildIndex(p, pp, types, matchers);
        }

        return pp;
    }

    //todo ph: par 0 clean
    private void buildIndex(Parameter p, PreparedParameter pp, Type<?>[] types, Matcher[] matchers) {

        Type<?>[] ktypes = types;
        Matcher[] kmatchers = matchers;
        int k;

        if (p.isMultivalue()) {
            k = p.getInputLevels();                             // indeksujemy k pierwszych poziomow
            ktypes = Arrays.copyOf(types, k);                   // podtablica typow
            kmatchers = Arrays.copyOf(matchers, k);             // podtablica matcherow

        } else {
            k = p.getLevelCount();                              // indeksujemy wszystkie n poziomow (k = n)
        }


        LevelIndex<PreparedEntry> index = new LevelIndex<PreparedEntry>(k, ktypes, kmatchers);   // indeks k-poziomowy

        for (ParameterEntry pe : p.getEntries()) {
            String[] keys = pe.getLevels(k);                                                   // pobranie k pierwszych poziomow
            index.add(keys, prepareEntry(pe));                                                 // indeksujemy k poziomow
        }

        pp.setIndex(index);
    }

    private PreparedEntry prepareEntry(ParameterEntry pe) {
        PreparedEntry e = new PreparedEntry();

        e.setLevels(pe.getLevels());
        e.setValue(pe.getValue());
        if(pe.getFunction() != null) {
            e.setFunction(functionProvider.getFunction(pe.getFunction()));
        }

        return e;
    }

    @Override
    public List<PreparedEntry> findEntries(String paramName, String[] levelValues) {
        List<ParameterEntry> entries = paramRepository.findEntries(paramName, levelValues);

        List<PreparedEntry> result = new ArrayList<PreparedEntry>(entries.size());
        for (ParameterEntry pe : entries) {
            result.add(prepareEntry(pe));
        }

        return result;
    }

    public ParamCache getParamCache() {
        return cache;
    }

    public void setParamCache(ParamCache cache) {
        this.cache = cache;
    }

    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

    public ParamRepository getParamRepository() {
        return paramRepository;
    }

    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    public TypeRepository getTypeRepository() {
        return typeProvider;
    }

    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeProvider = typeRepository;
    }

    public MatcherRepository getMatcherRepository() {
        return matcherProvider;
    }

    public void setMatcherRepository(SmartMatcherRepository matcherRepository) {
        this.matcherProvider = matcherRepository;
    }
}
