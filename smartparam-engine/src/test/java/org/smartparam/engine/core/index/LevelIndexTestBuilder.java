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
package org.smartparam.engine.core.index;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class LevelIndexTestBuilder {

    private int levels;

    private List<Type<?>> indexTypes = new ArrayList<Type<?>>();

    private List<Matcher> levelMatchers = new ArrayList<Matcher>();

    private LevelIndexTestBuilder() {
    }

    public static LevelIndexTestBuilder levelIndex() {
        return new LevelIndexTestBuilder();
    }

    public <T> LevelIndex<T> build() {
        Type<?>[] types = indexTypes.toArray(new Type<?>[indexTypes.size()]);
        Matcher[] matchers = levelMatchers.toArray(new Matcher[levelMatchers.size()]);

        return new LevelIndex<T>(levels, types, matchers);
    }

    public LevelIndexTestBuilder withLevelCount(int levelCount) {
        this.levels = levelCount;
        return this;
    }

    public LevelIndexTestBuilder withMatcher(Matcher matcher) {
        levelMatchers.add(matcher);
        return this;
    }

    public LevelIndexTestBuilder withType(Type<?> type) {
        indexTypes.add(type);
        return this;
    }
}
