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

import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleJdbcRepositoryTest extends DatabaseTest {

    @Test
    public void shouldReturnParameterWithLevelsAndEntries() {
        // given
        database().withParameter(1, "test").withParameterEntries(1, 5)
                .withLevels(1, 4).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);

        // when
        Parameter parameter = repository.getParameter("test");

        // then
        assertThat(parameter).hasName("test").hasLevels(4).hasEntries(5);
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

        Parameter parameter = parameter().withName("test").withLevels(levels).withEntries(entries).build();
        SimpleJdbcRepository repository = get(SimpleJdbcRepository.class);

        // when
        repository.createParameter(parameter);

        // then
        assertDatabase().hasParameter("test").hasLevelsForParameter("test", 2)
                .hasEntriesForParameter("test", 3).close();
    }
}