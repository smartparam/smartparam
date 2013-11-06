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
public class ParameterForm {

    private String name;

    private boolean nameChanged;

    private int inputLevels;

    private boolean inputLevelsChanged;

    private boolean cacheable;

    private boolean cacheableChanged;

    private boolean nullable;

    private boolean nullableChanged;

    private char arraySeparator;

    private boolean arraySeparatorChanged;

    public ParameterForm() {
    }

    public String getName() {
        return name;
    }

    public ParameterForm updateName(String newName) {
        this.name = newName;
        nameChanged = true;
        return this;
    }

    public boolean hasNameChanged() {
        return nameChanged;
    }

    public int getInputLevels() {
        return inputLevels;
    }

    public ParameterForm updateInputLevels(int newInputLevels) {
        this.inputLevels = newInputLevels;
        inputLevelsChanged = true;
        return this;
    }

    public boolean hasInputLevelsChanged() {
        return inputLevelsChanged;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public ParameterForm updateCacheable(boolean newCacheableValue) {
        this.cacheable = newCacheableValue;
        cacheableChanged = true;
        return this;
    }

    public boolean hasCacheableChanged() {
        return cacheableChanged;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ParameterForm updateNullable(boolean newNullableValue) {
        this.nullable = newNullableValue;
        nullableChanged = true;
        return this;
    }

    public boolean hasNullableChanged() {
        return nullableChanged;
    }

    public char getArraySeparator() {
        return arraySeparator;
    }

    public ParameterForm updateArraySeparator(char newArraySeparator) {
        this.arraySeparator = newArraySeparator;
        this.arraySeparatorChanged = true;
        return this;
    }

    public boolean hasArraySeparatorChanged() {
        return arraySeparatorChanged;
    }

}
