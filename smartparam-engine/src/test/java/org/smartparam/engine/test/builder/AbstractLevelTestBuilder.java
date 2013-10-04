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

    protected final T level;

    protected AbstractLevelTestBuilder(T instance) {
        this.level = instance;
    }

    protected abstract B self();

    public T build() {
        return level;
    }

    public B withName(String name) {
        level.setName(name);
        return self();
    }

    public B withMatcher(String matcher) {
        level.setMatcher(matcher);
        return self();
    }

    public B withLevelCreator(String levelCreator) {
        level.setLevelCreator(levelCreator);
        return self();
    }

    public B withType(String type) {
        level.setType(type);
        return self();
    }

    public B array() {
        level.setArray(true);
        return self();
    }
}
