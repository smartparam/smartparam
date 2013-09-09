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

package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.model.Level;

/**
 *
 * @author Adam Dubiel
 */
public class LevelAssert extends AbstractAssert<LevelAssert, Level> {

    private LevelAssert(Level actual) {
        super(actual, LevelAssert.class);
    }

    public static LevelAssert assertThat(Level actual) {
        return new LevelAssert(actual);
    }

    public LevelAssert hasName(String name) {
        Assertions.assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public LevelAssert hasType(String type) {
        Assertions.assertThat(actual.getType()).isEqualTo(type);
        return this;
    }

    public LevelAssert hasMatcher(String matcher) {
        Assertions.assertThat(actual.getMatcher()).isEqualTo(matcher);
        return this;
    }

    public LevelAssert hasLevelCreator(String levelCreator) {
        Assertions.assertThat(actual.getLevelCreator()).isEqualTo(levelCreator);
        return this;
    }

    public LevelAssert isArray() {
        Assertions.assertThat(actual.isArray()).isTrue();
        return this;
    }
}