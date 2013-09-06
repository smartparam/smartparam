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
package org.smartparam.repository.jdbc.core.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;

/**
 *
 * @author Adam Dubiel
 */
public class Query {

    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\:[A-Za-z]*");

    private static final String QUERY_PLACEHOLDER = "?";

    private String originalQuery;

    private String query;

    private Map<String, QueryArgument> arguments = new HashMap<String, QueryArgument>();

    private List<QueryArgument> orderedArguments;

    private Query(String query) {
        this.originalQuery = query;
        this.query = query;
    }

    public static Query query(String query) {
        return new Query(query);
    }

    public String getQuery() {
        return query;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public Query setInt(String argumentName, int value) {
        addArgument(argumentName, value, QueryArgumentType.INT);
        return this;
    }

    public Query setLong(String argumentName, long value) {
        addArgument(argumentName, value, QueryArgumentType.LONG);
        return this;
    }

    public Query setBoolean(String argumentName, boolean value) {
        addArgument(argumentName, value, QueryArgumentType.BOOLEAN);
        return this;
    }

    public Query setChar(String argumentName, char value) {
        addArgument(argumentName, value, QueryArgumentType.CHAR);
        return this;
    }

    public Query setString(String argumentName, String value) {
        addArgument(argumentName, value, QueryArgumentType.STRING);
        return this;
    }

    public Query replaceString(String argumentName, String value) {
        if (!query.contains(":" + argumentName)) {
            throw new SmartParamJdbcException("Could not set JdbcQuery value for argument " + argumentName + ". Argument does not exist in query: " + originalQuery);
        }
        query = query.replaceAll("\\:" + argumentName, value);
        return this;
    }

    private void addArgument(String placeholderName, Object value, QueryArgumentType type) {
        arguments.put(placeholderName, new QueryArgument(value, type));
    }

    public void injectValues(PreparedStatement preparedStatement) throws SQLException {
        int index = 1;
        for (QueryArgument argument : orderedArguments) {
            preparedStatement.setObject(index, argument.getValue(), argument.getSqlType());
            index++;
        }
    }

    public void compile() {
        orderedArguments = new ArrayList<QueryArgument>();

        Matcher matcher = ARGUMENT_PATTERN.matcher(query);
        String foundPattern;
        while (matcher.find()) {
            foundPattern = matcher.group();
            orderedArguments.add(arguments.get(foundPattern.replace(":", "")));
        }
        query = matcher.replaceAll(QUERY_PLACEHOLDER);
    }

    @Override
    public String toString() {
        return query;
    }
}
