/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.repository.memory;

import java.util.Collection;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.testng.annotations.Test;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParameterEntryBatchLoaderTest {

    @Test
    public void shouldReturnTrueIfThereAreMoreEntriesWaitingToBeFetchedWhenAskingIfHasMore() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addEntry(new InMemoryParameterEntry());
        InMemoryParameterEntryBatchLoader batchLoader = new InMemoryParameterEntryBatchLoader(parameter);

        // when
        boolean hasMore = batchLoader.hasMore();

        // then
        assertThat(hasMore).isTrue();
    }

    @Test
    public void shouldLoadBatchOfEntriesWhenAskingForNextBatchOfGivenSize() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addEntry(new InMemoryParameterEntry());
        parameter.addEntry(new InMemoryParameterEntry());
        parameter.addEntry(new InMemoryParameterEntry());
        InMemoryParameterEntryBatchLoader batchLoader = new InMemoryParameterEntryBatchLoader(parameter);

        // when
        Collection<ParameterEntry> entries = batchLoader.nextBatch(2);

        // then
        assertThat(entries).hasSize(2);
    }

    @Test
    public void shouldReturnRestOfEntriesWhenAskingForLastBatchWhichMightBeSmallerThanAskedFor() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addEntry(new InMemoryParameterEntry());
        InMemoryParameterEntryBatchLoader batchLoader = new InMemoryParameterEntryBatchLoader(parameter);

        // when
        Collection<ParameterEntry> entries = batchLoader.nextBatch(2);

        // then
        assertThat(entries).hasSize(1);
    }

    @Test
    public void shouldReturnFalseIfAllEntriesHaveBeenReturnedWhenAskingIfHasMore() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addEntry(new InMemoryParameterEntry());
        InMemoryParameterEntryBatchLoader batchLoader = new InMemoryParameterEntryBatchLoader(parameter);

        // when
        batchLoader.nextBatch(2);
        boolean hasMore = batchLoader.hasMore();

        // then
        assertThat(hasMore).isFalse();
    }

}
