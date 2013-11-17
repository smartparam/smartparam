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
package org.smartparam.engine.model.editable;

import org.smartparam.engine.model.SimpleLevel;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleEditableLevel extends SimpleLevel implements EditableLevel {

    @Override
    public LevelKey getKey() {
        return new SimpleLevelKey(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    @Override
    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setArray(boolean array) {
        this.array = array;
    }
}
