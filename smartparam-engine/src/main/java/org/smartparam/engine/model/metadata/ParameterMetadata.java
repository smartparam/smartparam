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
public class ParameterMetadata {

    private final String name;

    private final int inputLevels;

    private final boolean cacheable;

    private final boolean nullable;

    private final char arraySeparator;

    public ParameterMetadata(String name, int inputLevels, boolean cacheable, boolean nullable, char arraySeparator) {
        this.name = name;
        this.inputLevels = inputLevels;
        this.cacheable = cacheable;
        this.nullable = nullable;
        this.arraySeparator = arraySeparator;
    }

    public String getName() {
        return name;
    }

    public int getInputLevels() {
        return inputLevels;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public char getArraySeparator() {
        return arraySeparator;
    }
}
