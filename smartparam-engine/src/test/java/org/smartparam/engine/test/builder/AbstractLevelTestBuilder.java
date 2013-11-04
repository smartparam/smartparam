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

import org.smartparam.engine.model.editable.EditableLevel;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractLevelTestBuilder<T extends EditableLevel, B extends AbstractLevelTestBuilder<?, ?>> {

    private String name;

    private String matcher;

    private String levelCreator;

    private String type;

    private boolean array;

    protected AbstractLevelTestBuilder() {
    }

    protected abstract B self();

    protected abstract T buildLevel();

    public T build() {
        T level = buildLevel();
        level.setName(name);
        level.setMatcher(matcher);
        level.setLevelCreator(levelCreator);
        level.setType(type);
        level.setArray(array);

        return level;
    }

    public B withName(String name) {
        this.name = name;
        return self();
    }

    public B withMatcher(String matcher) {
        this.matcher = matcher;
        return self();
    }

    public B withLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
        return self();
    }

    public B withType(String type) {
        this.type = type;
        return self();
    }

    public B array() {
        this.array = true;
        return self();
    }
}
