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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.serializer.config.DefaultSerializationConfig;
import org.supercsv.io.CsvListReader;
import static org.assertj.core.api.Assertions.*;
import static org.smartparam.serializer.entries.CsvPreferenceBuilder.csvPreference;
import static org.smartparam.serializer.test.builder.CsvEntriesReaderTestBuilder.csvEntriesReader;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntryBatchLoaderTest {

    private final DefaultSerializationConfig config = new DefaultSerializationConfig();

    @Before
    public void initialize() {
    }

    @Test
    public void shouldDeserializeGivenAmountOfParameterEntriesFromReader() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvListReader csvReader = new CsvListReader(reader, csvPreference(config));
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(csvReader);

        // when
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        entries.addAll(batchLoader.nextBatch(5));
        entries.addAll(batchLoader.nextBatch(5));

        // then
        assertThat(entries).hasSize(10);
    }

    @Test
    public void shouldReturnLessThanRequestedBatchSizeIfNothingMoreToRead() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvListReader csvReader = new CsvListReader(reader, csvPreference(config));
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(csvReader);

        // when
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        entries.addAll(batchLoader.nextBatch(15));

        // then
        assertThat(entries).hasSize(10);
    }

    @Test
    public void shouldReturnEmptyCollectionForAnyBatchRequestAfterReadingAllEntries() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvListReader csvReader = new CsvListReader(reader, csvPreference(config));
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(csvReader);
        batchLoader.nextBatch(11);

        // when
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        entries.addAll(batchLoader.nextBatch(15));

        // then
        assertThat(entries).isEmpty();
    }

    @Test
    public void shouldReturnTrueWhenAskedIfHasMoreAndNotAllEntriesWereRead() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvListReader csvReader = new CsvListReader(reader, csvPreference(config));
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(csvReader);

        // when
        batchLoader.nextBatch(5);

        // then
        assertThat(batchLoader.hasMore()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAskedIfHasMoreAndAllEntriesWereRead() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvListReader csvReader = new CsvListReader(reader, csvPreference(config));
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(csvReader);

        // when
        batchLoader.nextBatch(11);

        // then
        assertThat(batchLoader.hasMore()).isFalse();
    }
}
