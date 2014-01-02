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
package org.smartparam.repository.jdbc.model;

import org.smartparam.editor.model.AbstractEntityKey;
import org.smartparam.editor.model.ParameterKey;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterKey extends AbstractEntityKey implements ParameterKey {

    static final String SYMBOL = "jdbc";

    private final String value;

    private final long parameterId;

    public JdbcParameterKey(long parameterId) {
        this.value = format(SYMBOL, Long.toString(parameterId));
        this.parameterId = parameterId;
    }

    public JdbcParameterKey(ParameterKey parameterKey) {
        String[] segments = parse(SYMBOL, parameterKey.value());
        value = parameterKey.value();
        parameterId = Long.parseLong(segments[0]);
    }

    @Override
    public String value() {
        return value;
    }

    public long parameterId() {
        return parameterId;
    }

}
