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
package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.engine.PreparedLevel;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 */
public class PreparedLevelTestBuilder {

    private String name;

    private boolean array;

    private Matcher matcher;

    private Type<?> type;

    private Function levelCreator;

    private PreparedLevelTestBuilder() {
    }

    public static PreparedLevelTestBuilder preparedLevel() {
        return new PreparedLevelTestBuilder();
    }

    public PreparedLevel build() {
        return new PreparedLevel(name, array, type, matcher, levelCreator);
    }

    public PreparedLevelTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PreparedLevelTestBuilder array() {
        this.array = true;
        return this;
    }

    public PreparedLevelTestBuilder withMatcher(Matcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public PreparedLevelTestBuilder withType(Type<?> type) {
        this.type = type;
        return this;
    }

    public PreparedLevelTestBuilder withLevelCreator(Function levelCreator) {
        this.levelCreator = levelCreator;
        return this;
    }
}
