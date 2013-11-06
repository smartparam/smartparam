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
package org.smartparam.repository.jdbc.model;

import org.smartparam.engine.model.EntityKey;
import org.smartparam.engine.util.EngineUtil;
import org.smartparam.engine.core.exception.InvalidEntityKeyException;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcEntityKey implements EntityKey {

    private static final char SEPARATOR = '#';

    private static final String JDBC_PREFIX = "jdbc";

    private final long id;

    private final String parameterName;

    private final String key;

    public JdbcEntityKey(long id, String parameterName) {
        this.id = id;
        this.parameterName = parameterName;
        this.key = constructKey(id, parameterName);
    }

    private String constructKey(long id, String parameterName) {
        return JDBC_PREFIX + SEPARATOR + parameterName + SEPARATOR + Long.toString(id);
    }

    public static JdbcEntityKey parseKey(EntityKey entityKey) throws InvalidEntityKeyException {
        if (entityKey instanceof JdbcEntityKey) {
            return (JdbcEntityKey) entityKey;
        } else if (entityKey.getKey().startsWith(JDBC_PREFIX)) {
            String[] parts = EngineUtil.split(entityKey.getKey(), '#', 3);
            if (parts.length != 3) {
                throw createException(entityKey);
            }

            String parameterName = parts[1];
            long id = Long.parseLong(parts[2]);

            return new JdbcEntityKey(id, parameterName);
        }
        throw createException(entityKey);
    }

    private static InvalidEntityKeyException createException(EntityKey entityKey) {
        return new InvalidEntityKeyException("Provided key can't be parsed as JdbcEntityKey. Key: " + entityKey);
    }

    public long getId() {
        return id;
    }

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "[JDBC entity key: " + getKey() + "]";
    }
}
