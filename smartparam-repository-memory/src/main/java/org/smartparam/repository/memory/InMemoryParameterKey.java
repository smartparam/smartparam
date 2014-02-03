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

import java.util.UUID;
import org.smartparam.engine.core.parameter.identity.AbstractEntityKey;
import org.smartparam.engine.core.parameter.ParameterKey;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParameterKey extends AbstractEntityKey implements ParameterKey {

    static final String SYMBOL = "memory";

    private final String value;

    InMemoryParameterKey() {
        value = super.format(SYMBOL, UUID.randomUUID().toString());
    }

    InMemoryParameterKey(ParameterKey key) {
        super.parse(SYMBOL, key.value());
        this.value = key.value();
    }

    @Override
    public String value() {
        return value;
    }

}
