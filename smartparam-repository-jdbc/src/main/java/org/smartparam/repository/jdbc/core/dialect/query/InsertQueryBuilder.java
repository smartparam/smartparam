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
package org.smartparam.repository.jdbc.core.dialect.query;

import org.smartparam.repository.jdbc.core.dialect.Dialect;
import org.smartparam.repository.jdbc.util.StringBuilderUtil;

/**
 *
 * @author Adam Dubiel
 */
public class InsertQueryBuilder {

    private static final int QUERY_LENGTH = 100;

    private Dialect dialect;

    private StringBuilder queryStem = new StringBuilder(QUERY_LENGTH);

    private StringBuilder columns = new StringBuilder(QUERY_LENGTH / 2);

    private StringBuilder values = new StringBuilder(QUERY_LENGTH / 2);

    private InsertQueryBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public static InsertQueryBuilder query(Dialect dialect) {
        return new InsertQueryBuilder(dialect);
    }

    public String build() {
        StringBuilderUtil.trimLastCharacters(columns, 2);
        StringBuilderUtil.trimLastCharacters(values, 2);

        queryStem.append("(").append(columns).append(")")
                .append(" VALUES(").append(values).append(")");
        return queryStem.toString();
    }

    public InsertQueryBuilder into(String tableName) {
        queryStem.append("INSERT INTO ").append(tableName);
        return this;
    }

    public InsertQueryBuilder id(String idColumnName, String sequenceName) {
        return value(idColumnName, dialect.getProperties().nextFromSequence(sequenceName));
    }

    public InsertQueryBuilder value(String columnName, String value) {
        columns.append(columnName).append(", ");
        values.append(value).append(", ");
        return this;
    }
}
