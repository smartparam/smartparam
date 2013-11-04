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
package org.smartparam.engine.model.metadata;

/**
 *
 * @author Adam Dubiel
 */
public class LevelMetadata {

    private final String name;

    private final String levelCreator;

    private final String matcher;

    private final String type;

    private final boolean array;

    public LevelMetadata(String name, String levelCreator, String matcher, String type, boolean array) {
        this.name = name;
        this.levelCreator = levelCreator;
        this.matcher = matcher;
        this.type = type;
        this.array = array;
    }

    public String getName() {
        return name;
    }

    public String getLevelCreator() {
        return levelCreator;
    }

    public String getMatcher() {
        return matcher;
    }

    public String getType() {
        return type;
    }

    public boolean isArray() {
        return array;
    }
}
