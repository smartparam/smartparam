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
public class LevelForm {

    private String name;

    private boolean nameChanged;

    private String levelCreator;

    private boolean levelCreatorChanged;

    private String matcher;

    private boolean matcherChanged;

    private String type;

    private boolean typeChanged;

    private boolean array;

    private boolean arrayChanged;

    public LevelForm() {
    }

    public String getName() {
        return name;
    }

    public LevelForm rename(String name) {
        this.name = name;
        nameChanged = true;
        return this;
    }

    public boolean nameChanged() {
        return nameChanged;
    }

    public String getLevelCreator() {
        return levelCreator;
    }

    public LevelForm udpateLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
        levelCreatorChanged = true;
        return this;
    }

    public boolean hasLevelCreatorChanged() {
        return levelCreatorChanged;
    }

    public String getMatcher() {
        return matcher;
    }

    public LevelForm udpateMatcher(String matcher) {
        this.matcher = matcher;
        matcherChanged = true;
        return this;
    }

    public boolean hasMatcherChanged() {
        return matcherChanged;
    }

    public String getType() {
        return type;
    }

    public LevelForm udpateType(String type) {
        this.type = type;
        typeChanged = true;
        return this;
    }

    public boolean hasTypeChanged() {
        return typeChanged;
    }

    public boolean isArray() {
        return array;
    }

    public LevelForm udpateArray(boolean array) {
        this.array = array;
        arrayChanged = true;
        return this;
    }

    public boolean hasArrayChanged() {
        return arrayChanged;
    }
}
