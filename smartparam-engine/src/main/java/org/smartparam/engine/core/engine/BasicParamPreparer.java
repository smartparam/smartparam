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
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public class BasicParamPreparer implements ParamPreparer {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private TypeRepository typeProvider;

    private MatcherRepository matcherProvider;

    private ParameterProvider parameterProvider;

    private FunctionProvider functionProvider;

    private ParamCache cache;

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

            Type<?> type = resolveType(level.getType(), parameter.getName(), currentLevelIndex);
            Matcher matcher = resolveMatcher(level.getMatcher(), parameter.getName(), currentLevelIndex);
            Function levelCreator = resolveLevelCreator(level.getLevelCreator());

            levels[currentLevelIndex] = new PreparedLevel(level.getName(), type, level.isArray(), matcher, levelCreator);
            types[currentLevelIndex] = type;
            matchers[currentLevelIndex] = matcher;
        }

        PreparedParameter preparedParameter = new PreparedParameter(parameter, levels);
        preparedParameter.setLevelNameMap(buildLevelNameToIndexMap(preparedParameter));

        if (parameter.isCacheable()) {
            preparedParameter.setIndex(buildIndex(parameter, types, matchers));
        }

        return preparedParameter;
    }

    private Type<?> resolveType(String typeCode, String parameterName, int levelIndex) {
        Type<?> type = null;
        if (typeCode != null) {
            type = typeProvider.getType(typeCode);

            if (type == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                        String.format("Level[%d] of parameter %s has unknown type %s. "
                        + "To see all registered types, look for MapRepository logs on INFO level during startup.",
                        levelIndex, parameterName, typeCode));
            }
        }
        return type;
    }

    private Matcher resolveMatcher(String matcherCode, String parameterName, int levelIndex) {
        Matcher matcher = null;
        if (matcherCode != null) {
            matcher = matcherProvider.getMatcher(matcherCode);

            if (matcher == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_MATCHER,
                        String.format("Level[%d] of parameter %s has unknown matcher %s. "
                        + "To see all registered matchers, look for MapRepository logs on INFO level during startup.",
                        (levelIndex), parameterName, matcherCode));
            }
        }
        return matcher;
    }

    private Function resolveLevelCreator(String levelCreatorCode) {
        Function levelCreator = null;
        if (levelCreatorCode != null) {
            levelCreator = functionProvider.getFunction(levelCreatorCode);
        }
        return levelCreator;
    }

    private LevelIndex<PreparedEntry> buildIndex(Parameter parameter, Type<?>[] types, Matcher[] matchers) {
        int inputLevelCount = parameter.getInputLevels();
        Type<?>[] inputLevelTypes = Arrays.copyOf(types, inputLevelCount);
        Matcher[] inputLevelMatchers = Arrays.copyOf(matchers, inputLevelCount);

        LevelIndex<PreparedEntry> index = new LevelIndex<PreparedEntry>(inputLevelCount, inputLevelTypes, inputLevelMatchers);

        String[] keys;
        for (ParameterEntry parameterEntry : parameter.getEntries()) {
            keys = getFirstNLevels(parameterEntry, inputLevelCount);
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
                nameMap.put(level.getName(), currentLevelIndex + 1 - inputLevelCount);
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
