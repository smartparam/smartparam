/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.repository.jdbc;

import java.util.Arrays;
import org.polyjdbc.core.query.TransactionRunner;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.testng.annotations.Test;

import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParamRepositoryTest extends DatabaseTest {

    @Test
    public void shouldReturnParameterWithLevelsAndEntries() {
        // given
        database().withParameter(1, "test").withLevels(1, 5).withParameterEntries(1, 5).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        Parameter parameter = repository.load("test");

        // then
        assertThat(parameter).isNotNull().hasName("test")
                .hasLevels(5).hasEntries(5);
    }

    @Test
    public void shouldReturnParameterBatchLoader() {
        // given
        database().withParameter(1, "test").withLevels(1, 5).withParameterEntries(1, 5).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        ParameterBatchLoader loader = repository.batchLoad("test");

        // then
        assertThat(loader).hasEntryLoader().hasMetadataFor("test").hasMetadataWithLevels(5);
    }

    @Test
    public void shouldWriteNewParameterIntoDatabase() {
        // given
        Level[] levels = new Level[]{level().withName("lvl1").withType("string").build()};
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        Parameter parameter = parameter().withName("test").withLevels(levels).withEntries(entries).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.write(parameter);

        // then
        assertDatabase().hasParameter("test").hasEntriesForParameter("test", 1).hasLevelsForParameter("test", 1).close();
    }

    @Test
    public void shouldOverwriteExistingParameterWithSameName() {
        // given
        database().withParameter(1, "test").withLevels(1, 2).withParameterEntries(1, 2).build();
        Level[] levels = new Level[]{level().withName("lvl1").withType("string").build()};
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        Parameter parameter = parameter().withName("test").withLevels(levels).withEntries(entries).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.write(parameter);

        // then
        assertDatabase().hasParameter("test").hasEntriesForParameter("test", 1).hasLevelsForParameter("test", 1).close();
    }

    @Test
    public void shouldAppendEntriesToExistingParameters() {
        // given
        database().withParameter(1, "test").withLevels(1, 1).withParameterEntries(1, 1).build();
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.writeParameterEntries("test", Arrays.asList(entries));

        // then
        assertDatabase().hasParameter("test").hasEntriesForParameter("test", 2).close();
    }
}