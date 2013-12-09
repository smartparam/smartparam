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
package org.smartparam.repository.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.smartparam.engine.test.ParamEngineAssertions;
import org.smartparam.repository.jdbc.config.DefaultJdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;
import org.testng.annotations.Test;

import static org.smartparam.repository.jdbc.config.JdbcConfigBuilder.jdbcConfig;
import static org.smartparam.repository.jdbc.test.builder.ResultSetMockBuilder.resultSet;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryMapperTest {

    @Test
    public void shouldReturnEntryWithSameAmountOfLevelsWhenLevelsBelowLimit() throws SQLException {
        // given
        DefaultJdbcConfig configuration = jdbcConfig().withLevelColumnCount(10).build();
        JdbcParameterEntryMapper mapper = new JdbcParameterEntryMapper(configuration);
        ResultSet resultSet = resultSet().withLong("id", 1).withLong("fk_parameter", 1)
                .withString("level1", "1").withString("level2", "2").build();

        // when
        JdbcParameterEntry entry = mapper.createObject(resultSet);

        // then
        ParamEngineAssertions.assertThat(entry).hasLevels(2);
    }

    @Test
    public void shouldReturnEntryWithMaximumLevelsWhenLevelsEqualsLimit() throws SQLException {
        // given
        DefaultJdbcConfig configuration = jdbcConfig().withLevelColumnCount(2).build();
        JdbcParameterEntryMapper mapper = new JdbcParameterEntryMapper(configuration);
        ResultSet resultSet = resultSet().withLong("id", 1).withLong("fk_parameter", 1)
                .withString("level1", "1").withString("level2", "2")
                .build();

        // when
        JdbcParameterEntry entry = mapper.createObject(resultSet);

        // then
        ParamEngineAssertions.assertThat(entry).hasLevels(2);
    }

    @Test
    public void shouldReturnEntryWithLevelsExtractedFromSplitLastLevelWhenLevelCountGreaterThanLimit() throws SQLException {
        // given
        DefaultJdbcConfig configuration = jdbcConfig()
                .withLevelColumnCount(3).withExcessLevelSeparator('|').build();
        JdbcParameterEntryMapper mapper = new JdbcParameterEntryMapper(configuration);
        ResultSet resultSet = resultSet().withLong("id", 1).withLong("fk_parameter", 1)
                .withString("level1", "1").withString("level2", "2")
                .withString("level3", "3|4|5")
                .build();

        // when
        JdbcParameterEntry entry = mapper.createObject(resultSet);

        // then
        ParamEngineAssertions.assertThat(entry).hasLevels(5);
    }
}