package org.smartparam.serializer.entries;

import org.junit.Before;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.serializer.StandardSerializationConfig;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntrySerializerIntegrationTest {

    private CsvParameterEntrySerializer serializer;

    private CsvParameterEntryDeserializer deserializer;

    private StandardSerializationConfig config = new StandardSerializationConfig();

    @Before
    public void initialize() {
        serializer = new CsvParameterEntrySerializer();
        deserializer = new CsvParameterEntryDeserializer(SimpleEditableParameterEntry.class);
    }

//    @Test
//    public void testSerializationAndDeserialization() throws SmartParamSerializationException {
//        ParameterEntrySupplierMock supplier = new ParameterEntrySupplierMock(100, 20, 5);
//        List<String> header = Arrays.asList("h1", "h2", "h3", "h4", "h5");
//
//        StringWriter stringWriter = new StringWriter();
//        serializer.serialize(config, stringWriter, supplier);
//        String csv = stringWriter.toString();
//
//        ParameterEntryPersisterMock persister = new ParameterEntryPersisterMock(10);
//        StringReader stringReader = new StringReader(csv);
//        deserializer.deserialize(config, stringReader, persister);
//
//        assertEquals(10, persister.getWriteBatchCallCount());
//        assertEquals(100, persister.getEntries().size());
//    }
}
