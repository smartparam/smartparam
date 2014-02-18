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
package org.smartparam.engine.core.prepared;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterFromRepository;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class BasicParamPreparer implements ParamPreparer {

    private final LevelPreparer levelPreparer;

    public BasicParamPreparer(LevelPreparer levelPreparer) {
        this.levelPreparer = levelPreparer;
    }

    @Override
    public PreparedParameter prepare(ParameterFromRepository parameterFromRepository) {
        Parameter parameter = parameterFromRepository.parameter();

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

        PreparedParameter preparedParameter = new PreparedParameter(parameterFromRepository.repositoryName(), parameter, levels);
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
                    keys[i] = InputValueNormalizer.normalize(types[i], keys[i]);
                }
            }

            index.add(keys, prepareEntry(parameterEntry, parameter.isIdentifyEntries()));
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

    @Override
    public PreparedEntry prepareIdentifiableEntry(ParameterEntry parameterEntry) {
        return new IdentifiablePreparedEntry(parameterEntry);
    }

    private PreparedEntry prepareEntry(ParameterEntry parameterEntry, boolean identifyEntries) {
        return identifyEntries ? new IdentifiablePreparedEntry(parameterEntry) : new PreparedEntry(parameterEntry);
    }
}
