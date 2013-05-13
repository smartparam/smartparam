package org.smartparam.serializer.entries;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.smartparam.serializer.mock.EditableParameterEntryMock;
import org.smartparam.serializer.mock.ParameterEntryPersisterMock;
import org.smartparam.serializer.mock.ParameterEntrySupplierMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializerTest {

    private CsvParameterEntrySerializer serializer;

    private SerializationConfig config = new SerializationConfig('"', ';', "\n");

    @Before
    public void initialize() {
        serializer = new CsvParameterEntrySerializer(EditableParameterEntryMock.class);
    }

    @Test
    public void testSerialization() throws SmartParamSerializationException {
        ParameterEntrySupplierMock supplier = new ParameterEntrySupplierMock(100, 20, 5);

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(config, stringWriter, supplier);

        String result = stringWriter.toString();
        assertEquals(5, supplier.getCalledForNextBatchCount());
        assertTrue(result.contains("level_99_4"));
    }

    @Test
    public void testDeserialization() throws SmartParamSerializationException {
        ParameterEntryPersisterMock persister = new ParameterEntryPersisterMock(10);

        StringBuilder csvBuilder = new StringBuilder("some;header;to;ignore\n");
        int entriesToDeserialzie = 45;
        for(int index = 0; index < entriesToDeserialzie; ++index) {
            csvBuilder.append(String.format("pe_%1$s_1;pe_%1$s_2;pe_%1$s_3;pe_%1$s_4\n", index));
        }

        StringReader stringReader = new StringReader(csvBuilder.toString());
        serializer.deserialize(config, stringReader, persister);

        assertEquals(5, persister.getWriteBatchCallCount());
        assertEquals(45, persister.getEntries().size());
        assertEquals("pe_44_1", persister.getEntries().get(44).getLevels()[0]);
    }

    @Test
    public void testSerializationAndDeserialization() throws SmartParamSerializationException {
        ParameterEntrySupplierMock supplier = new ParameterEntrySupplierMock(100, 20, 5);
        List<String> header = Arrays.asList("h1", "h2", "h3", "h4", "h5");

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(config, stringWriter, supplier);
        String csv = stringWriter.toString();

        ParameterEntryPersisterMock persister = new ParameterEntryPersisterMock(10);
        StringReader stringReader = new StringReader(csv);
        serializer.deserialize(config, stringReader, persister);

        assertEquals(10, persister.getWriteBatchCallCount());
        assertEquals(100, persister.getEntries().size());
    }
}
