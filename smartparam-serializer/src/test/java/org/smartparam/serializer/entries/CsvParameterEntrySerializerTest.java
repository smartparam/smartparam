package org.smartparam.serializer.entries;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.smartparam.serializer.mock.ParameterEntrySupplierMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializerTest {

    private CsvParameterEntrySerializer serializer;

    private StandardSerializationConfig config = new StandardSerializationConfig('"', ';', '#', "\n");

    @Before
    public void initialize() {
        serializer = new CsvParameterEntrySerializer();
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
// TODO #ad move to integration test
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
//        serializer.deserialize(config, stringReader, persister);
//
//        assertEquals(10, persister.getWriteBatchCallCount());
//        assertEquals(100, persister.getEntries().size());
//    }
}
