package org.smartparam.engine.core.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.config.SmartMatcherProvider;
import org.smartparam.engine.core.config.TypeProvider;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.core.type.AbstractType;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * Klasa dostarcza przygotowane parametry na podstawie nazwy.
 * Wykorzystuje cache, poniewaz przygotowanie parametru jest kosztowne.
 * <p>
 *
 * Przygotowanie parametru sklada sie z 3 glownych krokow:
 * <ol>
 * <li> wczytanie parametru przy pomocy <i>loadera</i> (np. z bazy danych),
 * <li> zamiana struktury Parameter/ParameterEntry na blizniacza strukture PreparedParameter/PreparedEntry,
 * <li> zbudowanie indeksu wyszukiwania dla wczytanego parametru.
 * </ol>
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public class SmartParamPreparer implements ParamPreparer {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(ParamEngine.class);

    /**
     * Dostep do systemu typow silnika.
     */
    private TypeProvider typeProvider;

    /**
     * Dostep do systemu matcherow.
     */
    private SmartMatcherProvider matcherProvider;

    /**
     * Loader parametrow.
     */
    private ParamLoader loader;

    /**
     * Cache.
     */
    private ParamCache cache;

    @Override
    public PreparedParameter getPreparedParameter(String paramName) {

        PreparedParameter pp = cache.get(paramName);

        if (pp == null) {
            Parameter p = loader.load(paramName);

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
     * Buduje przygotowany (skompilowany) parametr na podstawie
     * wczytanego przez <tt>loader</tt> parametru <tt>p</tt>.
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
        AbstractType<?> paramType = typeProvider.getType(p.getType());
        pp.setType(paramType);

        if (paramType == null && !p.isMultivalue()) {
            throw new ParamDefinitionException(
                    SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                    "Parameter " + p.getName() + " has undefined param type: " + p.getType());
        }

        /*
         * przygotowanie konfiguracji poziomow (typy, matchery)
         */

        int levelCount = p.getLevelCount();
        PreparedLevel[] levels = new PreparedLevel[levelCount];
        AbstractType<?>[] types = new AbstractType<?>[levelCount];
        Matcher[] matchers = new Matcher[levelCount];

        for (int i = 0; i < levelCount; i++) {
            Level lev = p.getLevel(i);

            AbstractType<?> type = null;
            if (lev.getType() != null) {
                type = typeProvider.getType(lev.getType());

                if (type == null) {
                    throw new ParamDefinitionException(
                            SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                            "Parameter " + p.getName() + ": level(" + (i + 1) + ") has unknown type: " + lev.getType());
                }
            }

            Matcher matcher = null;
            if (lev.getMatcherCode() != null) {
                matcher = matcherProvider.getMatcher(lev.getMatcherCode());

                if (matcher == null) {
                    throw new ParamDefinitionException(
                            SmartParamErrorCode.UNKNOWN_MATCHER,
                            "Parameter " + p.getName() + ": level(" + (i + 1) + ") has unknown matcher: " + lev.getMatcherCode());
                }
            }

            levels[i] = new PreparedLevel(type, lev.isArray(), matcher, lev.getLevelCreator());
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
    private void buildIndex(Parameter p, PreparedParameter pp, AbstractType<?>[] types, Matcher[] matchers) {

        AbstractType<?>[] ktypes = types;
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
        e.setFunction(pe.getFunction());

        return e;
    }

    @Override
    public List<PreparedEntry> findEntries(String paramName, String[] levelValues) {
        List<ParameterEntry> entries = loader.findEntries(paramName, levelValues);

        List<PreparedEntry> result = new ArrayList<PreparedEntry>(entries.size());
        for (ParameterEntry pe : entries) {
            result.add(prepareEntry(pe));
        }

        return result;
    }

    public void setCache(ParamCache cache) {
        this.cache = cache;
    }

    public void setLoader(ParamLoader loader) {
        this.loader = loader;
    }

    public void setTypeProvider(TypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }

    public void setMatcherProvider(SmartMatcherProvider matcherProvider) {
        this.matcherProvider = matcherProvider;
    }
}
