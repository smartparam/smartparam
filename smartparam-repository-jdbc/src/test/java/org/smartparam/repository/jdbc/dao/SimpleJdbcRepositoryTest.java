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

import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.jdbc.DatabaseTest;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.ParamEngineAssertions.*;
import static org.smartparam.engine.core.parameter.LevelTestBuilder.level;
import static org.smartparam.engine.core.parameter.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleJdbcRepositoryTest extends DatabaseTest {

    @Test
    public void shouldReturnParameterWithLevelsAndEntries() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 5)
                .withLevels("parameter", 4).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        Parameter parameter = repository.getParameter(runner, "parameter");
        runner.close();

        // then
        assertThat(parameter).hasName("parameter").hasLevels(4).hasEntries(5);
    }

    @Test
    public void shouldReturnParameterMetadataWithoutEntries() {
        // given
        database().withParameter("parameter").withParameterEntries("parameter", 5)
                .withLevels("parameter", 4).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        Parameter metadata = repository.getParameterMetadata(runner, "parameter");
        runner.close();

        // then
        assertThat(metadata).hasName("parameter").hasLevels(4).hasNoEntries();
    }

    @Test
    public void shouldInsertNewParameterWithLevelsAndEntries() {
        // given
        Level[] levels = new Level[]{
            level().withName("level").withType("string").build(),
            level().withName("level").withType("string").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("test").build(),
            parameterEntry().withLevels("test").build(),
            parameterEntry().withLevels("test").build()
        };

        Parameter parameter = parameter().withName("parameter").withLevels(levels).withEntries(entries).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        repository.createParameter(runner, parameter);
        runner.close();

        // then
        assertDatabase().hasParameter("parameter").hasLevelsForParameter("parameter", 2)
                .hasEntriesForParameter("parameter", 3).close();
    }

    @Test
    public void shouldReturnSetOfParameterNames() {
        // given
        database().withParameter("test1").withParameter("test2").build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);

        // when
        Set<String> parameters = repository.listParameterNames();

        // then
        assertThat(parameters).hasSize(2);
    }

    @Test
    public void shouldCheckIfParameterExists() {
        // given
        database().withParameter("parameter").build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        boolean exists = repository.parameterExists(runner, "parameter");
        runner.close();

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void shouldDeleteParameterWithLevelsAndEntries() {
        // given
        database().withParameter("parameter").withLevels("parameter", 5).withParameterEntries("parameter", 5).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        repository.deleteParameter(runner, "parameter");
        runner.close();

        // then
        assertDatabase().hasNoParameter("parameter").close();
    }

    @Test
    public void shouldDeleteAllEntriesOfParameter() {
        // given
        database().withParameter("parameter").withLevels("parameter", 5).withParameterEntries("parameter", 5).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);
        QueryRunner runner = queryRunner();

        // when
        repository.deleteParameterEntries(runner, "parameter");
        runner.close();

        // then
        assertDatabase().hasNoEntriesForParameter("parameter").close();
    }
}
