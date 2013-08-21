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

import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 */
public class BasicLevelPreparer implements LevelPreparer {

    private MatcherRepository matcherRepository;

    private TypeRepository typeRepository;

    private FunctionProvider functionProvider;

    @Override
    public PreparedLevel prepare(Level level) {
        Type<?> type = resolveType(level.getType(), level.getName());
        Matcher matcher = resolveMatcher(level.getMatcher(), level.getName());
        Function levelCreator = resolveLevelCreator(level.getLevelCreator());

        return new PreparedLevel(level.getName(), type, level.isArray(), matcher, levelCreator);
    }

    private Type<?> resolveType(String typeCode, String levelName) {
        Type<?> type = null;
        if (typeCode != null) {
            type = typeRepository.getType(typeCode);

            if (type == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_PARAM_TYPE,
                        String.format("Level %s has unknown type %s. "
                        + "To see all registered types, look for MapRepository logs on INFO level during startup.",
                        levelName, typeCode));
            }
        }
        return type;
    }

    private Matcher resolveMatcher(String matcherCode, String levelName) {
        Matcher matcher = null;
        if (matcherCode != null) {
            matcher = matcherRepository.getMatcher(matcherCode);

            if (matcher == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_MATCHER,
                        String.format("Level %s has unknown matcher %s. "
                        + "To see all registered matchers, look for MapRepository logs on INFO level during startup.",
                        levelName, matcherCode));
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

    @Override
    public MatcherRepository getMatcherRepository() {
        return matcherRepository;
    }

    @Override
    public void setMatcherRepository(MatcherRepository matcherRepository) {
        this.matcherRepository = matcherRepository;
    }

    @Override
    public TypeRepository getTypeRepository() {
        return typeRepository;
    }

    @Override
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    @Override
    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }
}
