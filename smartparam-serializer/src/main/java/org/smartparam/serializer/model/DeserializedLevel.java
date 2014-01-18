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
package org.smartparam.serializer.model;

import org.smartparam.engine.core.parameter.identity.EmptyEntityKey;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.level.LevelKey;

/**
 *
 * @author Adam Dubiel
 */
public class DeserializedLevel implements Level {

    private String name;

    private String levelCreator;

    private String type;

    private String matcher;

    private boolean array;

    public DeserializedLevel() {
    }

    public DeserializedLevel(Level level) {
        this.name = level.getName();
        this.levelCreator = level.getLevelCreator();
        this.type = level.getType();
        this.matcher = level.getMatcher();
        this.array = level.isArray();
    }

    @Override
    public LevelKey getKey() {
        return EmptyEntityKey.emptyKey();
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
