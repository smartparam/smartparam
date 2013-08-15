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
package org.smartparam.repository.jdbc.query;

import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcQuery {

    private String originalQuery;

    private String query;

    private JdbcQuery(String query) {
        this.originalQuery = query;
        this.query = query;
    }

    public static JdbcQuery query(String query) {
        return new JdbcQuery(query);
    }

    public String getQuery() {
        return query;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setInt(String argumentName, int value) {
        setValue(argumentName, Integer.toString(value));
    }

    public void setString(String argumentName, String value) {
        setValue(argumentName, "'" + value + "'");
    }

    private void setValue(String argumentName, String value) {
        if(!query.contains(":" + argumentName)) {
            throw new SmartParamJdbcException("Could not set JdbcQuery value for argument " + argumentName + ". Argument does not exist in query: " + originalQuery);
        }
        query = query.replaceFirst("\\:" + argumentName, value);
    }

    @Override
    public String toString() {
        return query;
    }


}
