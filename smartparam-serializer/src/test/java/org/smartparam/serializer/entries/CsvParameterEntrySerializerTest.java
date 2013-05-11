package org.smartparam.serializer.entries;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.mock.EditableParameterEntryMock;
import org.smartparam.serializer.mock.ParameterEntrySupplierMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializerTest {

    private CsvParameterEntrySerializer serializer;

    @Before
    public void initialize() {
        SerializationConfig config = new SerializationConfig('"', ';', "\n");
        serializer = new CsvParameterEntrySerializer(config, EditableParameterEntryMock.class);
    }

    @Test
    public void testSerialization() {
        ParameterEntrySupplierMock supplier = new ParameterEntrySupplierMock(100, 20, 5);
        List<String> header = Arrays.asList("h1", "h2", "h3", "h4", "h5");

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(stringWriter, header, supplier);
        stringWriter.flush();

        String result = stringWriter.toString();
        assertEquals(5, supplier.getCalledForNextBatchCount());
        assertTrue(result.contains("level_99_4"));
    }
}
