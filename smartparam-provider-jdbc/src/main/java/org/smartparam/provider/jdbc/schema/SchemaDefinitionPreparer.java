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
package org.smartparam.provider.jdbc.schema;

import org.smartparam.provider.jdbc.config.Configuration;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SchemaDefinitionPreparer {

    private static final String PARAMETER_TABLE_NAME = "\\:parameterTableName";

    private static final String LEVEL_TABLE_NAME = "\\:levelTableName";

    private static final String PARAMETER_ENTRY_TABLE_NAME = "\\:parameterEntryTableName";

    public String prepareQuery(String query, Configuration configuration) {
        String preparedQuery = query.replaceAll(PARAMETER_TABLE_NAME, configuration.getParameterTable());
        preparedQuery = preparedQuery.replaceAll(LEVEL_TABLE_NAME, configuration.getParameterLevelTable());
        preparedQuery = preparedQuery.replaceAll(PARAMETER_ENTRY_TABLE_NAME, configuration.getParameterEntryTable());

        return preparedQuery;
    }
}
