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
package org.smartparam.repository.jdbc.test.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel
 */
public class ResultSetMockBuilder {

    private ResultSet resultSet;

    private ResultSetMockBuilder() {
        resultSet = mock(ResultSet.class);
    }

    public static ResultSetMockBuilder resultSet() {
        return new ResultSetMockBuilder();
    }

    public ResultSet build() {
        return resultSet;
    }

    public ResultSetMockBuilder withString(String name, String value) throws SQLException {
        when(resultSet.getString(name)).thenReturn(value);
        return this;
    }

    public ResultSetMockBuilder withLong(String name, long value) throws SQLException {
        when(resultSet.getLong(name)).thenReturn(value);
        return this;
    }
}
