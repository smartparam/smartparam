/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.core.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.ParameterProvider;
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
public class SmartParamPreparer implements ParamPreparer {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    /**
     * Dostep do systemu typow silnika.
     */
    private TypeRepository typeProvider;

    /**
     * Dostep do systemu matcherow.
     */
    private MatcherRepository matcherProvider;

    /**
     * Dostep do parametrow.
     */
    private ParameterProvider parameterProvider;

    private FunctionProvider functionProvider;

    /**
     * Cache.
     */
    private ParamCache cache;

    @Override
    public PreparedParameter getPreparedParameter(String paramName) {

        PreparedParameter pp = cache.get(paramName);

        if (pp == null) {
            Parameter p = parameterProvider.load(paramName);

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
        pp.setInputLevelsCount(p.getInputLevels());
        pp.setNullable(p.isNullable());
        pp.setCacheable(p.isCacheable());
        pp.setArraySeparator(p.getArraySeparator());

        /*
         * przygotowanie konfiguracji poziomow (typy, matchery)
         */

        int levelCount = getLevelCount(p);
        PreparedLevel[] levels = new PreparedLevel[levelCount];
        Type<?>[] types = new Type<?>[levelCount];
        Matcher[] matchers = new Matcher[levelCount];

        for (int i = 0; i < levelCount; i++) {
            Level lev = getLevel(p, i);

            Type<?> type = null;
            if (lev.getType() != null) {
                type = typeProvider.getType(lev.getType());

                if (type == null) {
                    throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                            String.format("Level[%d] of parameter %s has unknown type %s. " +
                                    "To see all registered types, look for MapRepository logs on INFO level during startup.",
                                    (i + 1), p.getName(), lev.getType()));
                }
            }

            Matcher matcher = null;
            if (lev.getMatcher() != null) {
                matcher = matcherProvider.getMatcher(lev.getMatcher());

                if (matcher == null) {
                    throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_MATCHER,
                            String.format("Level[%d] of parameter %s has unknown matcher %s. " +
                                    "To see all registered matchers, look for MapRepository logs on INFO level during startup.",
                                    (i + 1), p.getName(), lev.getMatcher()));
                }
            }

            Function levelCreator = null;
            if(lev.getLevelCreator() != null) {
                    levelCreator = functionProvider.getFunction(lev.getLevelCreator());
            }
            levels[i] = new PreparedLevel(lev.getName(), type, lev.isArray(), matcher, levelCreator);
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

		/*
		 * build level name mapping
		 */
		buildOutputLevelMap(pp);

        return pp;
    }

    private void buildIndex(Parameter p, PreparedParameter pp, Type<?>[] types, Matcher[] matchers) {

        int k = p.getInputLevels();                         // indeksujemy k pierwszych poziomow
        Type<?>[] ktypes = Arrays.copyOf(types, k);         // podtablica typow
        Matcher[] kmatchers = Arrays.copyOf(matchers, k);   // podtablica matcherow

        LevelIndex<PreparedEntry> index = new LevelIndex<PreparedEntry>(k, ktypes, kmatchers);   // indeks k-poziomowy

        for (ParameterEntry pe : p.getEntries()) {
            String[] keys = getFirstLevels(pe, k);          // pobranie k pierwszych poziomow
            index.add(keys, prepareEntry(pe));              // indeksujemy k poziomow
        }

        pp.setIndex(index);
    }

	/**
	 * Construct level name to position mapping.
	 */
	private void buildOutputLevelMap(PreparedParameter pp) {

		Map<String, Integer> nameMap = new LinkedHashMap<String, Integer>();

		int inputCnt = pp.getInputLevelsCount();	// number of input levels
		int allCnt = pp.getLevelCount();			// number of all levels

		// all levels
		PreparedLevel[] levels = pp.getLevels();

		for (int i = inputCnt; i < allCnt; i++) {
			PreparedLevel level = levels[i];
			if (level.getName() != null) {
				nameMap.put(level.getName(), i + 1 - inputCnt);
			}
		}

		pp.setLevelNameMap(nameMap);
	}

    private int getLevelCount(Parameter p) {
        List<? extends Level> levels = p.getLevels();
        return levels != null ? levels.size() : 0;
    }

    private Level getLevel(Parameter p, int index) {
        return p.getLevels().get(index);
    }

    /**
     * Returns patterns for first k levels.
     *
     * @param k number of levels
     * @return values of first k levels
     */
    protected String[] getFirstLevels(ParameterEntry pe, int k) {
        return Arrays.copyOf(pe.getLevels(), k);
    }

    private PreparedEntry prepareEntry(ParameterEntry parameterEntry) {
        return new PreparedEntry(parameterEntry);
    }

    @Override
    public List<PreparedEntry> findEntries(String paramName, String[] levelValues) {
        Set<ParameterEntry> entries = parameterProvider.findEntries(paramName, levelValues);

        List<PreparedEntry> result = new ArrayList<PreparedEntry>(entries.size());
        for (ParameterEntry pe : entries) {
            result.add(prepareEntry(pe));
        }

        return result;
    }

    @Override
    public ParamCache getParamCache() {
        return cache;
    }

    @Override
    public void setParamCache(ParamCache cache) {
        this.cache = cache;
    }

    @Override
    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    @Override
    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

    @Override
    public ParameterProvider getParameterProvider() {
        return parameterProvider;
    }

    @Override
    public void setParameterProvider(ParameterProvider parameterProvider) {
        this.parameterProvider = parameterProvider;
    }

    @Override
    public TypeRepository getTypeRepository() {
        return typeProvider;
    }

    @Override
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeProvider = typeRepository;
    }

    @Override
    public MatcherRepository getMatcherRepository() {
        return matcherProvider;
    }

    @Override
    public void setMatcherRepository(MatcherRepository matcherRepository) {
        this.matcherProvider = matcherRepository;
    }
}
