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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.editor.core.filters.SortDirection;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.test.Iterables;
import org.smartparam.repository.jdbc.config.JdbcConfigBuilder;
import org.smartparam.repository.jdbc.DatabaseTest;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;
import org.testng.annotations.Test;

import static org.smartparam.engine.core.parameter.entry.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.Iterables.onlyElement;
import static org.smartparam.engine.test.ParamEngineAssertions.*;

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
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        ParameterEntry entry = parameterEntry().withLevels("1", "2").build();

        // when
        parameterEntryDAO.insert(runner, Arrays.asList(entry), parameter.getId());
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
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        ParameterEntry entry = parameterEntry().withLevels("1", "2", "3", "4").build();

        // when
        parameterEntryDAO.insert(runner, Arrays.asList(entry), parameter.getId());
        runner.commit();

        Set<JdbcParameterEntry> entries = parameterEntryDAO.getJdbcParameterEntries(runner, "parameter");
        runner.close();

        // then
        assertThat(onlyElement(entries)).hasLevels(4).levelAtEquals(2, "3").levelAtEquals(3, "4");
    }

    @Test
    public void shouldReturnListOfEntriesWithGivenIdsInSameOrderAsIds() {
        // given
        List<Long> ids = new ArrayList<Long>();
        database().withParameter("parameter").withParameterEntries("parameter", 5, ids).build();
        Collections.shuffle(ids);

        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        // when
        List<ParameterEntry> entries = parameterEntryDAO.getParameterEntries(runner, ids);
        runner.close();

        // then
        assertThat(entries).hasSize(5).isSortedAccordingTo(new ParameterEntryIdSequenceComparator(ids));
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

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        long entryIdToDelete = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), parameter.getId());
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

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        long entryIdToUpdate = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), parameter.getId());
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

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        long entryIdToUpdate = parameterEntryDAO.insert(runner, parameterEntry().withLevels("1").build(), parameter.getId());
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

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1").build(),
                parameterEntry().withLevels("2").build());
        List<Long> entriesIdsToDelete = parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        parameterEntryDAO.delete(runner, entriesIdsToDelete);
        runner.close();

        // then
        assertDatabase().hasNoEntriesForParameter("parameter").close();
    }

    @Test
    public void shouldLimitNumberOfEntriesReturned() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 10).build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");

        // when
        List<ParameterEntry> entries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter(0, 5));
        runner.close();

        // then
        assertThat(entries).hasSize(5);
    }

    @Test
    public void shouldSelectEntriesWithGivenOffset() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 9).build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(ParameterDAO.class).getParameter(runner, "parameter");

        // when
        List<ParameterEntry> entries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter(1, 5));
        runner.close();

        // then
        assertThat(entries).hasSize(4);
    }

    @Test
    public void shouldFilterEntriesUsingProvidedSingleFilterExpression() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2").build(),
                parameterEntry().withLevels("1", "3").build(),
                parameterEntry().withLevels("5", "2").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().filter("level1", "1"));
        runner.close();

        // then
        assertThat(foundEntries).hasSize(2);
    }

    @Test
    public void shouldFilterEntriesUsingProvidedMultipleFilterExpressions() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2").build(),
                parameterEntry().withLevels("1", "5").build(),
                parameterEntry().withLevels("5", "2").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().filter("level1", "5").filter("level2", "2"));
        runner.close();

        // then
        assertThat(foundEntries).hasSize(1);
    }

    @Test
    public void shouldNotFilterContentsOfConcatenatedExcessLevelsEvenIfFilterProvided() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2", "level3").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2", "3").build(),
                parameterEntry().withLevels("5", "6").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().filter("level3", "3"));
        runner.close();

        // then
        assertThat(foundEntries).hasSize(2);
    }

    @Test
    public void shouldTranslateAntSyntaxToSQLLikeSyntaxForFiltering() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2", "level3").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("prefix-12", "2", "3").build(),
                parameterEntry().withLevels("does-not-match-12", "2", "3").build(),
                parameterEntry().withLevels("other-prefix-52", "6").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter,
                new ParameterEntriesFilter().filter("level1", "*prefix*"));
        runner.close();

        // then
        assertThat(foundEntries).hasSize(2);
    }

    @Test
    public void shouldUseOrStarNotationToMathStarsWhenFiltering() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2").build(),
                parameterEntry().withLevels("5", "*").build(),
                parameterEntry().withLevels("5", "2").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().filter("level1", "5").filterOrStar("level2", "2"));
        runner.close();

        // then
        assertThat(foundEntries).hasSize(2);
    }

    @Test
    public void shouldOrderBySelectedLevelAndDirection() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2").build(),
                parameterEntry().withLevels("5", "6").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().orderBy("level2", SortDirection.DESC));
        runner.close();

        // then
        assertThat(foundEntries.get(0)).hasLevels("5", "6");
    }

    @Test
    public void shouldNotOrderByConcatenatedExcessLevelsEvenIfToldToDoSo() {
        // given
        database().withParameter("parameter").withLevels("parameter", "level1", "level2", "level3").build();
        ParameterEntryDAO parameterEntryDAO = get(ParameterEntryDAO.class);
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = get(JdbcRepository.class).getParameterMetadata(runner, "parameter");
        List<ParameterEntry> entries = Arrays.asList(
                parameterEntry().withLevels("1", "2", "3").build(),
                parameterEntry().withLevels("5", "6", "7").build());
        parameterEntryDAO.insert(runner, entries, parameter.getId());
        runner.commit();

        // when
        List<ParameterEntry> foundEntries = parameterEntryDAO.list(runner, parameter, new ParameterEntriesFilter().orderBy("level3", SortDirection.DESC));
        runner.close();

        // then
        assertThat(foundEntries.get(0)).hasDifferentLevelsThan("5", "6", "7");
    }
}
