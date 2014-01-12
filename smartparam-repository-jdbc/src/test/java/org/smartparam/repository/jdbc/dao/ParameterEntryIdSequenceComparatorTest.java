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
package org.smartparam.repository.jdbc.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.repository.jdbc.test.builder.JdbcParameterEntryTestBuilder.jdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryIdSequenceComparatorTest {

    @Test
    public void shouldSortEntriesInOrderOfProvidedIdSequence() {
        // given
        List<JdbcParameterEntry> entries = Arrays.asList(
                jdbcParameterEntry().withId(2).build(),
                jdbcParameterEntry().withId(8).build(),
                jdbcParameterEntry().withId(10).build(),
                jdbcParameterEntry().withId(5).build()
        );

        List<Long> sequence = Arrays.asList(10L, 5L, 8L, 2L);
        ParameterEntryIdSequenceComparator comparator = new ParameterEntryIdSequenceComparator(sequence);

        // when
        Collections.sort(entries, comparator);
        List<Long> sequenceAfterOrdering = extractIds(entries);

        // then
        assertThat(sequenceAfterOrdering).startsWith(10L, 5L, 8L, 2L);
    }

    private List<Long> extractIds(List<JdbcParameterEntry> entries) {
        List<Long> sequenceAfterOrdering = new ArrayList<Long>();
        for (JdbcParameterEntry entry : entries) {
            sequenceAfterOrdering.add(entry.getId());
        }
        return sequenceAfterOrdering;
    }

}
