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
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.testng.annotations.Test;

import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;
import static org.smartparam.engine.core.parameter.level.LevelTestBuilder.level;
import static org.smartparam.engine.core.parameter.entry.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParamRepositoryTest extends DatabaseTest {

    @Test
    public void shouldReturnParameterWithLevelsAndEntries() {
        // given
        database().withParameter("parameter").withLevels("parameter", 5).withParameterEntries("parameter", 5).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        Parameter parameter = repository.load("parameter");

        // then
        assertThat(parameter).isNotNull().hasName("parameter")
                .hasLevels(5).hasEntries(5);
    }

    @Test
    public void shouldReturnParameterBatchLoader() {
        // given
        database().withParameter("parameter").withLevels("parameter", 5).withParameterEntries("parameter", 5).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        ParameterBatchLoader loader = repository.batchLoad("parameter");

        // then
        assertThat(loader).hasEntryLoader().hasMetadataFor("parameter").hasMetadataWithLevels(5);
    }

    @Test
    public void shouldWriteNewParameterIntoDatabase() {
        // given
        Level[] levels = new Level[]{level().withName("lvl1").withType("string").build()};
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        Parameter parameter = parameter().withName("parameter").withLevels(levels).withEntries(entries).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.write(parameter);

        // then
        assertDatabase().hasParameter("parameter").hasEntriesForParameter("parameter", 1).hasLevelsForParameter("parameter", 1).close();
    }

    @Test
    public void shouldOverwriteExistingParameterWithSameName() {
        // given
        database().withParameter("parameter").withLevels("parameter", 2).withParameterEntries("parameter", 2).build();
        Level[] levels = new Level[]{level().withName("lvl1").withType("string").build()};
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        Parameter parameter = parameter().withName("parameter").withLevels(levels).withEntries(entries).build();
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.write(parameter);

        // then
        assertDatabase().hasParameter("parameter").hasEntriesForParameter("parameter", 1).hasLevelsForParameter("parameter", 1).close();
    }

    @Test
    public void shouldAppendEntriesToExistingParameters() {
        // given
        database().withParameter("parameter").withLevels("parameter", 1).withParameterEntries("parameter", 1).build();
        ParameterEntry[] entries = new ParameterEntry[]{parameterEntry().withLevels("A").build()};
        JdbcParamRepository repository = get(JdbcParamRepository.class);

        // when
        repository.writeParameterEntries("parameter", Arrays.asList(entries));

        // then
        assertDatabase().hasParameter("parameter").hasEntriesForParameter("parameter", 2).close();
    }
}
