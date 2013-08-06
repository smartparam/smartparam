package org.smartparam.serializer.entries;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.serializer.StandardSerializationConfig;
import static org.fest.assertions.api.Assertions.*;
import static org.smartparam.serializer.test.builder.CsvEntriesReaderTestBuilder.csvEntriesReader;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryBatchLoaderTest {

    private StandardSerializationConfig config = new StandardSerializationConfig();

    @Before
    public void initialize() {
    }

    @Test
    public void shouldDeserializeGivenAmountOfParameterEntriesFromReader() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(1, "header", "to", "skip").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(SimpleEditableParameterEntry.class, config, new SimpleBatchReaderWrapper(reader));

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
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(1, "header", "to", "skip").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(SimpleEditableParameterEntry.class, config, new SimpleBatchReaderWrapper(reader));

        // when
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        entries.addAll(batchLoader.nextBatch(15));

        // then
        assertThat(entries).hasSize(10);
    }

    @Test
    public void shouldReturnEmptyCollectionForAnyBatchRequestAfterReadingAllEntries() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(1, "header", "to", "skip").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(SimpleEditableParameterEntry.class, config, new SimpleBatchReaderWrapper(reader));
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
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(1, "header", "to", "skip").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(SimpleEditableParameterEntry.class, config, new SimpleBatchReaderWrapper(reader));

        // when
        batchLoader.nextBatch(5);

        // then
        assertThat(batchLoader.hasMore()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAskedIfHasMoreAndAllEntriesWereRead() throws Exception {
        // given
        StringReader reader = csvEntriesReader(10).usingDelimiter(";").withEntries(1, "header", "to", "skip").withEntries(10, "entry_%d", "column1", "column2").build();
        CsvParameterEntryBatchLoader batchLoader = new CsvParameterEntryBatchLoader(SimpleEditableParameterEntry.class, config, new SimpleBatchReaderWrapper(reader));

        // when
        batchLoader.nextBatch(11);

        // then
        assertThat(batchLoader.hasMore()).isFalse();
    }
}
