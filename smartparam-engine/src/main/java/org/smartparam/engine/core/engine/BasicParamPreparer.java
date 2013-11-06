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
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.service.ParameterProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class BasicParamPreparer implements ParamPreparer {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private ParameterProvider parameterProvider;

    private LevelPreparer levelPreparer;

    private ParamCache cache;

    public BasicParamPreparer(ParameterProvider parameterProvider, LevelPreparer levelPreparer, ParamCache cache) {
        this.parameterProvider = parameterProvider;
        this.levelPreparer = levelPreparer;
        this.cache = cache;
    }

    @Override
    public PreparedParameter getPreparedParameter(String paramName) {
        PreparedParameter preparedParameter = cache.get(paramName);

        if (preparedParameter == null) {
            Parameter parameter = parameterProvider.load(paramName);

            if (parameter == null) {
                logger.warn("parameter {} not found", paramName);
                return null;
            }

            preparedParameter = prepare(parameter);
            cache.put(paramName, preparedParameter);
        }

        return preparedParameter;
    }

    private PreparedParameter prepare(Parameter parameter) {
        int levelCount = getLevelCount(parameter);
        PreparedLevel[] levels = new PreparedLevel[levelCount];
        Type<?>[] types = new Type<?>[levelCount];
        Matcher[] matchers = new Matcher[levelCount];

        for (int currentLevelIndex = 0; currentLevelIndex < levelCount; currentLevelIndex++) {
            Level level = getLevel(parameter, currentLevelIndex);
            PreparedLevel preparedLevel = levelPreparer.prepare(level);

            levels[currentLevelIndex] = preparedLevel;
            types[currentLevelIndex] = preparedLevel.getType();
            matchers[currentLevelIndex] = preparedLevel.getMatcher();
        }

        PreparedParameter preparedParameter = new PreparedParameter(parameter, levels);
        preparedParameter.setLevelNameMap(buildLevelNameToIndexMap(preparedParameter));

        if (parameter.isCacheable()) {
            preparedParameter.setIndex(buildIndex(parameter, types, matchers));
        }

        return preparedParameter;
    }

    private LevelIndex<PreparedEntry> buildIndex(Parameter parameter, Type<?>[] types, Matcher[] matchers) {
        int inputLevelCount = parameter.getInputLevels();
        Type<?>[] inputLevelTypes = Arrays.copyOf(types, inputLevelCount);
        Matcher[] inputLevelMatchers = Arrays.copyOf(matchers, inputLevelCount);

        LevelIndex<PreparedEntry> index = new LevelIndex<PreparedEntry>(inputLevelCount, inputLevelTypes, inputLevelMatchers);

        String[] keys;
        for (ParameterEntry parameterEntry : parameter.getEntries()) {

            // raw level patterns (read from repository)
            keys = getFirstNLevels(parameterEntry, inputLevelCount);

            // normalize level patters
            for (int i = 0; i < inputLevelCount; i++) {
                if (matchers[i] == null) {
                    try {
                        String norm = types[i].decode(keys[i]).getString();
                        System.out.println("normalized ::: " + norm);
                        keys[i] = norm;
                    } catch (Exception e) {
                        System.err.println("failed norm");
                    }
                }
            }

            index.add(keys, prepareEntry(parameterEntry));
        }

        return index;
    }

    private Map<String, Integer> buildLevelNameToIndexMap(PreparedParameter preparedParameter) {
        Map<String, Integer> nameMap = new LinkedHashMap<String, Integer>();

        int inputLevelCount = preparedParameter.getInputLevelsCount();
        int levelCount = preparedParameter.getLevelCount();

        PreparedLevel[] levels = preparedParameter.getLevels();

        for (int currentLevelIndex = inputLevelCount; currentLevelIndex < levelCount; currentLevelIndex++) {
            PreparedLevel level = levels[currentLevelIndex];
            if (level.getName() != null) {
                nameMap.put(level.getName(), currentLevelIndex - inputLevelCount);
            }
        }

        return nameMap;
    }

    private int getLevelCount(Parameter parameter) {
        List<? extends Level> levels = parameter.getLevels();
        return levels != null ? levels.size() : 0;
    }

    private Level getLevel(Parameter parameter, int index) {
        return parameter.getLevels().get(index);
    }

    private String[] getFirstNLevels(ParameterEntry parameterEntry, int levelCount) {
        return Arrays.copyOf(parameterEntry.getLevels(), levelCount);
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
}
