package org.smartparam.serializer.entries;

import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.mgmt.test.mock.EditableParameterEntryMock;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.smartparam.serializer.mock.ParameterEntryPersisterMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryDeserializerTest {

    private CsvParameterEntryDeserializer deserializer;

    private StandardSerializationConfig config = new StandardSerializationConfig('"', ';', '#', "\n");

    @Before
    public void initialize() {
        deserializer = new CsvParameterEntryDeserializer(EditableParameterEntryMock.class);
    }

    @Test
    public void testDeserialization() throws SmartParamSerializationException {
        ParameterEntryPersisterMock persister = new ParameterEntryPersisterMock(10);

        StringBuilder csvBuilder = new StringBuilder("some;header;to;ignore\n");
        int entriesToDeserialzie = 45;
        for (int index = 0; index < entriesToDeserialzie; ++index) {
            csvBuilder.append(String.format("pe_%1$s_1;pe_%1$s_2;pe_%1$s_3;pe_%1$s_4\n", index));
        }

        StringReader stringReader = new StringReader(csvBuilder.toString());
        deserializer.deserialize(config, stringReader, persister);

        assertEquals(5, persister.getWriteBatchCallCount());
        assertEquals(45, persister.getEntries().size());
        assertEquals("pe_44_1", persister.getEntries().get(44).getLevels()[0]);
    }
}
