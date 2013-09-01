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

import org.smartparam.repository.jdbc.core.query.Query;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static com.googlecode.catchexception.CatchException.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class QueryTest {

    @Test
    public void shouldSetIntegerValueInPlaceholder() throws SQLException {
        // given
        Query query = Query.query("select * from test where count = :count");
        query.setInt("count", 1);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // when
        query.compile(preparedStatement);

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where count = ?");
        verify(preparedStatement, times(1)).setObject(0, 1, Types.INTEGER);
    }

    @Test
    public void shouldSetStringValueInPlaceholder() throws SQLException {
        // given
        Query query = Query.query("select * from test where name = :name");
        query.setString("name", "test");
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // when
        query.compile(preparedStatement);

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where name = ?");
        verify(preparedStatement, times(1)).setObject(0, "test", Types.VARCHAR);
    }

    @Test
    public void shouldSetMultipleValuesDuringCompilation() throws SQLException {
            // given
        Query query = Query.query("select * from test where name = :name and count = :count");
        query.setString("name", "test");
        query.setInt("count", 1);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // when
        query.compile(preparedStatement);

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where name = ? and count = ?");
        verify(preparedStatement, times(1)).setObject(0, "test", Types.VARCHAR);
        verify(preparedStatement, times(1)).setObject(1, 1, Types.INTEGER);
    }

    @Test
    public void shouldNotModifyOriginalQueryKeptAsReference() throws SQLException {
        // given
        Query query = Query.query("select * from test where name = :name");
        query.setString("name", "test");
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // when
        query.compile(preparedStatement);

        // then
        assertThat(query.getOriginalQuery()).isEqualTo("select * from test where name = :name");
    }

    @Test
    public void shouldReplaceStringLiteral() {
        // given
        Query query = Query.query("select * from :tableName");

        // when
        query.replaceString("tableName", "test");

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test");
    }

    @Test
    public void shouldThrowExceptionWhenTryingToReplaceNonexistingPlaceholderValue() {
        // given
        Query query = Query.query("select * from test");

        // when
        catchException(query).replaceString("unknown", "value");

        // then
        assertThat(caughtException()).isNotNull().isInstanceOf(SmartParamJdbcException.class);
    }
}