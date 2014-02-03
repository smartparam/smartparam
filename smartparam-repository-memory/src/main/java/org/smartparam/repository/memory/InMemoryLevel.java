/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.repository.memory;

import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.level.Level;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryLevel implements Level {

    private final InMemoryLevelKey key;

    private String name;

    private String levelCreator;

    private String type;

    private boolean array;

    private String matcher;

    InMemoryLevel() {
        this.key = new InMemoryLevelKey();
    }

    InMemoryLevel(Level level) {
        this();
        merge(level);
    }

    final void merge(Level level) {
        this.name = level.getName();
        this.levelCreator = level.getLevelCreator();
        this.type = level.getType();
        this.array = level.isArray();
        this.matcher = level.getMatcher();
    }

    @Override
    public LevelKey getKey() {
        return key;
    }

    InMemoryLevelKey getRawKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override

    public String getLevelCreator() {
        return levelCreator;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public String getMatcher() {
        return matcher;
    }

}
