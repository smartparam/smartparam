package org.smartparam.serializer.entries;

import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import java.io.StringWriter;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.serializer.StandardSerializationConfig;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializerTest {

    private CsvParameterEntrySerializer serializer;

    private StandardSerializationConfig config = new StandardSerializationConfig();

    private ParameterEntryBatchLoader entryBatchLoader;

    @Before
    public void initialize() {
        serializer = new CsvParameterEntrySerializer();
        entryBatchLoader = mock(ParameterEntryBatchLoader.class);
    }

    @Test
    public void shouldWriteParameterEntryHeaderUsingLevelNames() throws Exception {
        // given
        Level[] levels = new Level[]{
            level().withName("one").build(),
            level().withName("two").build()
        };
        Parameter parameter = parameter().withLevels(levels).build();
        StringWriter writer = new StringWriter();

        // when
        serializer.serialize(config, writer, parameter, entryBatchLoader);

        // then
        assertThat(writer.toString()).containsOnlyOnce("one;two");
    }

    @Test
    public void shouldSerializeParameterEntriesWithHeader() throws Exception {
        // given
        Level[] levels = new Level[]{
            level().withName("one").build(),
            level().withName("two").build()
        };
        Parameter parameter = parameter().withLevels(levels).build();
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("entry_one", "entry_one").build(),
            parameterEntry().withLevels("entry_two", "entry_two").build()
        };
        when(entryBatchLoader.hasMore()).thenAnswer(new EntryBatchLoaderHasMoreAnswer());
        when(entryBatchLoader.nextBatch(anyInt())).thenReturn(Arrays.asList(entries));
        StringWriter writer = new StringWriter();

        // when
        serializer.serialize(config, writer, parameter, entryBatchLoader);

        // then
        assertThat(writer.toString()).containsOnlyOnce("one;two")
                .containsOnlyOnce("entry_one;entry_one").containsOnlyOnce("entry_two;entry_two");
    }

    private static final class EntryBatchLoaderHasMoreAnswer implements Answer<Boolean> {

        private boolean hasMore = true;

        @Override
        public Boolean answer(InvocationOnMock invocation) throws Throwable {
            boolean returnValue = hasMore;
            hasMore = false;
            return returnValue;
        }
    }
}
