package org.smartparam.serializer.entries;

import java.io.IOException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.smartparam.serializer.SerializationConfig;
import org.supercsv.io.CsvListWriter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializer extends AbstractCsvParameterEntrySerializer implements ParameterEntrySerializer {

    private static final Logger logger = LoggerFactory.getLogger(CsvParameterEntrySerializer.class);

    @Override
    public void serialize(SerializationConfig config, Writer writer, ParameterEntrySupplier supplier) throws SmartParamSerializationException {
        CsvListWriter csvWriter = new CsvListWriter(writer, createCsvPreference(config));
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("started parameter entries serialization at {}", startTime);
            csvWriter.write(supplier.header());

            int counter = 0;
            while (supplier.hasMore()) {
                for (ParameterEntry entry : supplier.nextBatch()) {
                    csvWriter.write(entry.getLevels());
                    counter++;
                }
            }

            long endTime = System.currentTimeMillis();
            logger.debug("serializing {} parameter entries took {}", counter, endTime - startTime);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("serialization error", exception);
        } finally {
            closeWriter(csvWriter);
        }
    }

    private void closeWriter(CsvListWriter writer) throws SmartParamSerializationException {
        try {
            writer.close();
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while closing writer stream", exception);
        }
    }
}
