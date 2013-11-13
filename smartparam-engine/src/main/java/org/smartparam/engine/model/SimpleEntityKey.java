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
package org.smartparam.engine.model;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleEntityKey implements EntityKey {

    private final String key;

    private final String parameterName;

    public SimpleEntityKey(String key, String parameterName) {
        this.key = key;
        this.parameterName = parameterName;
    }

    public String getKey() {
        return key;
    }

    public String getParameterName() {
        return parameterName;
    }

    public long asNumber() {
        throw new UnsupportedOperationException("SimpleEntityKey has no long representation.");
    }

}
