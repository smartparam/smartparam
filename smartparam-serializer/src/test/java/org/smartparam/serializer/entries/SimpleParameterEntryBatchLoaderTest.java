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

package org.smartparam.serializer.entries;

import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import static org.fest.assertions.api.Assertions.*;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleParameterEntryBatchLoaderTest {

    private SimpleParameterEntryBatchLoader simpleParameterEntryBatchLoader;

    @Before
    public void initialize() {
    }

    @Test
    public void shouldReturnBatchOfEntries() {
        // given
        ParameterEntry[] entries = new ParameterEntry[] {
            parameterEntry().build(),
            parameterEntry().build()
        };
        Parameter parameter = parameter().withEntries(entries).build();
        simpleParameterEntryBatchLoader = new SimpleParameterEntryBatchLoader(parameter);

        // when
        Collection<ParameterEntry> loadedEntries = simpleParameterEntryBatchLoader.nextBatch(1);

        // then
        assertThat(loadedEntries).hasSize(1);
        assertThat(simpleParameterEntryBatchLoader.hasMore()).isTrue();
    }

    @Test
    public void shouldReturnLessThenBatchSizeIfNoMoreEntries() {
        // given
        ParameterEntry[] entries = new ParameterEntry[] {
            parameterEntry().build(),
            parameterEntry().build()
        };
        Parameter parameter = parameter().withEntries(entries).build();
        simpleParameterEntryBatchLoader = new SimpleParameterEntryBatchLoader(parameter);

        // when
        Collection<ParameterEntry> loadedEntries = simpleParameterEntryBatchLoader.nextBatch(10);

        // then
        assertThat(loadedEntries).hasSize(2);
        assertThat(simpleParameterEntryBatchLoader.hasMore()).isFalse();
    }
}