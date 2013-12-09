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
package org.smartparam.engine.core.parameter;

import org.smartparam.engine.core.parameter.Level;

/**
 *
 * @author Adam Dubiel
 */
public class LevelTestBuilder {

    private final TestLevel level = new TestLevel();

    public static LevelTestBuilder level() {
        return new LevelTestBuilder();
    }

    public Level build() {
        return level;
    }

    public LevelTestBuilder withName(String name) {
        level.name = name;
        return this;
    }

    public LevelTestBuilder withMatcher(String matcher) {
        level.matcher = matcher;
        return this;
    }

    public LevelTestBuilder withLevelCreator(String levelCreator) {
        level.levelCreator = levelCreator;
        return this;
    }

    public LevelTestBuilder withType(String type) {
        level.type = type;
        return this;
    }

    public LevelTestBuilder array() {
        level.array = true;
        return this;
    }
}
