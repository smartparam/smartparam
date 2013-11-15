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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.test.Iterables;
import org.smartparam.repository.jdbc.config.JdbcConfigBuilder;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;
import org.testng.annotations.Test;

import static org.smartparam.engine.test.Iterables.onlyElement;
import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class ParameterEntryDAOTest extends DatabaseTest {

    @Override
    protected void customizeConfiguraion(JdbcConfigBuilder builder) {
        builder.withExcessLevelSeparator('|').withLevelColumnCount(2);
    }

    @Test
    public void shouldInsertNewParameterEntry() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        ParameterEntry entry = parameterEntry().withLevels("1", "2").build();
        QueryRunner runner = queryRunner();

        // when
        parameterEntryDAO.insert(runner, Arrays.asList(entry), "parameter");
        runner.commit();

        Set<JdbcParameterEntry> entries = parameterEntryDAO.getJdbcParameterEntries(runner, "parameter");
        runner.close();

        // then
        assertThat(entries).hasSize(1);
    }

    @Test
    public void shouldConcatenateContentsOfExcessLevelsInLastLevelWhenInserting() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        ParameterEntry entry = parameterEntry().withLevels("1", "2", "3", "4").build();
        QueryRunner runner = queryRunner();

        // when
        parameterEntryDAO.insert(runner, Arrays.asList(entry), "parameter");
        runner.commit();

        Set<JdbcParameterEntry> entries = parameterEntryDAO.getJdbcParameterEntries(runner, "parameter");
        runner.close();

        // then
        assertThat(onlyElement(entries)).hasLevels(4).levelAtEquals(2, "3").levelAtEquals(3, "4");
    }

    @Test
    public void shouldListAllEntriesForParameter() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 5).build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        // when
        Set<JdbcParameterEntry> entries = parameterEntryDAO.getJdbcParameterEntries(runner, "parameter");
        runner.close();

        // then
        assertThat(entries).hasSize(5);
    }

    @Test
    public void shouldReturnBatchOfParameterEntriesOfGivenSize() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 100).build();

        // when
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        // when
        List<ParameterEntry> entries = parameterEntryDAO.getParameterEntriesBatch(runner, "parameter", 0, 20);
        runner.close();

        // then
        assertThat(entries).hasSize(20);
    }

    @Test
    public void shouldDeleteEntryWithGivenId() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        long entryIdToDelete = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), "parameter");
        runner.commit();

        // when
        parameterEntryDAO.delete(runner, entryIdToDelete);
        runner.close();

        // then
        assertDatabase().hasNoEntriesForParameter("parameter").close();
    }

    @Test
    public void shouldUpdateInformationOnParameterEntry() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        long entryIdToUpdate = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), "parameter");
        runner.commit();

        // when
        parameterEntryDAO.update(runner, entryIdToUpdate, parameterEntry().withLevels("1", "2").build());
        runner.commit();

        // then
        ParameterEntry updatedEntry = Iterables.firstItem(parameterEntryDAO.getParameterEntries(runner, "parameter"));
        runner.close();

        assertThat(updatedEntry).hasLevels("1", "2");
    }

    @Test
    public void shouldConcatenateContentsOfExcessLevelsInLastLevelWhenUpdating() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        long entryIdToUpdate = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), "parameter");
        runner.commit();

        // when
        parameterEntryDAO.update(runner, entryIdToUpdate, parameterEntry().withLevels("1", "2", "3", "4").build());
        runner.commit();

        // then
        ParameterEntry updatedEntry = Iterables.firstItem(parameterEntryDAO.getParameterEntries(runner, "parameter"));
        runner.close();

        assertThat(updatedEntry).hasLevels("1", "2", "3", "4");
    }

    @Test
    public void shouldDeleteEntriesWithGivenIds() {
        // given
        database().withParameter("parameter").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1").build(),
                parameterEntry().withLevels("2").build());
        List<Long> entriesIdsToDelete = parameterEntryDAO.insert(runner, entries, "parameter");
        runner.commit();

        // when
        parameterEntryDAO.delete(runner, entriesIdsToDelete);
        runner.close();

        // then
        assertDatabase().hasNoEntriesForParameter("parameter").close();
    }
}
