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

import org.smartparam.engine.model.Level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link Level} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class LevelMockBuilder {

    private Level level;

    private LevelMockBuilder() {
        this.level = mock(Level.class);
        when(level.isArray()).thenReturn(false);
    }

    public static LevelMockBuilder level() {
        return new LevelMockBuilder();
    }

    public static Level level(String type) {
        return level().withType(type).get();
    }

    public static Level level(String type, boolean array) {
        return level().withType(type).withArray(array).get();
    }

    public Level get() {
        return level;
    }

    public LevelMockBuilder withType(String type) {
        when(level.getType()).thenReturn(type);
        return this;
    }

    public LevelMockBuilder withLevelCreator(String levelCreator) {
        when(level.getLevelCreator()).thenReturn(levelCreator);
        return this;
    }

    public LevelMockBuilder withArray(boolean array) {
        when(level.isArray()).thenReturn(array);
        return this;
    }

    public LevelMockBuilder withMatcherCode(String matcher) {
        when(level.getMatcher()).thenReturn(matcher);
        return this;
    }
}
