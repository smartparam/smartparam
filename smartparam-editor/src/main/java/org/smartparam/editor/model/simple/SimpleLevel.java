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
package org.smartparam.editor.model.simple;

import org.smartparam.engine.core.parameter.Level;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleLevel implements Level {

    private String name;

    private String levelCreator;

    private String type;

    private boolean array;

    private String matcher;

    public SimpleLevel() {
    }

    public SimpleLevel(Level level) {
        this.name = level.getName();
        this.levelCreator = level.getLevelCreator();
        this.type = level.getType();
        this.matcher = level.getMatcher();
        this.array = level.isArray();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleLevel withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getLevelCreator() {
        return levelCreator;
    }

    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    public SimpleLevel withLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
        return this;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SimpleLevel withType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public SimpleLevel array() {
        this.array = true;
        return this;
    }

    @Override
    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public SimpleLevel withMatcher(String matcher) {
        this.matcher = matcher;
        return this;
    }

}
