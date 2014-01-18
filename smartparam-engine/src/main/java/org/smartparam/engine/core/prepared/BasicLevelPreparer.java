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

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.type.TypeRepository;
import org.smartparam.engine.core.function.FunctionProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.function.Function;

/**
 *
 * @author Adam Dubiel
 */
public class BasicLevelPreparer implements LevelPreparer {

    private final MatcherRepository matcherRepository;

    private final TypeRepository typeRepository;

    private final FunctionProvider functionProvider;

    public BasicLevelPreparer(MatcherRepository matcherRepository, TypeRepository typeRepository, FunctionProvider functionProvider) {
        this.matcherRepository = matcherRepository;
        this.typeRepository = typeRepository;
        this.functionProvider = functionProvider;
    }

    @Override
    public PreparedLevel prepare(Level level) {
        Type<?> type = resolveType(level.getType(), level.getName());
        Matcher matcher = resolveMatcher(level.getMatcher(), level.getName());
        Function levelCreator = resolveLevelCreator(level.getLevelCreator());

        return new PreparedLevel(level.getName(), level.isArray(), type, matcher, levelCreator);
    }

    private Type<?> resolveType(String typeCode, String levelName) {
        Type<?> type = null;
        if (typeCode != null) {
            type = typeRepository.getType(typeCode);

            if (type == null) {
                throw new UnknownTypeException(levelName, typeCode);
            }
        }
        return type;
    }

    private Matcher resolveMatcher(String matcherCode, String levelName) {
        Matcher matcher = null;
        if (matcherCode != null) {
            matcher = matcherRepository.getMatcher(matcherCode);

            if (matcher == null) {
                throw new UnknownMatcherException(levelName, matcherCode);
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
}
