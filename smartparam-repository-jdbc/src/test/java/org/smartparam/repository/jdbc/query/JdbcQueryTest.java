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

import org.smartparam.repository.jdbc.query.JdbcQuery;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcQueryTest {

    @Test
    public void shouldSetIntegerValueInPlaceholder() {
        // given
        JdbcQuery query = JdbcQuery.query("select * from test where count = :count");

        // when
        query.setInt("count", 1);

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where count = 1");
    }

    @Test
    public void shouldSetStringValueInPlaceholderEscapingItWithInvertedCommas() {
        // given
        JdbcQuery query = JdbcQuery.query("select * from test where name = :name");

        // when
        query.setString("name", "test");

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where name = 'test'");
    }

    @Test
    public void shouldNotModifyOriginalQueryKeptAsReference() {
        // given
        JdbcQuery query = JdbcQuery.query("select * from test where name = :name");

        // when
        query.setString("name", "test");

        // then
        assertThat(query.getOriginalQuery()).isEqualTo("select * from test where name = :name");
    }

    @Test
    public void shouldSetValuesInMultiplePlaceholders() {
        // given
        JdbcQuery query = JdbcQuery.query("select * from test where name = :name and count = :count");

        // when
        query.setInt("count", 1);
        query.setString("name", "test");

        // then
        assertThat(query.getQuery()).isEqualTo("select * from test where name = 'test' and count = 1");
    }

    @Test
    public void shouldThrowExceptionWhenTryingToSetNonexistingPlaceholderValue() {
        // given
        JdbcQuery query = JdbcQuery.query("select * from test");

        // when
        catchException(query).setString("unknown", "value");

        // then
        assertThat(caughtException()).isNotNull().isInstanceOf(SmartParamJdbcException.class);
    }
}