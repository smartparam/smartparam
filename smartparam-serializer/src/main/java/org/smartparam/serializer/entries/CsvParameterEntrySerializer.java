package org.smartparam.serializer.entries;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.smartparam.serializer.model.EditableParameterEntry;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntrySerializer implements ParameterEntrySerializer {

    private static final Logger logger = LoggerFactory.getLogger(CsvParameterEntrySerializer.class);

    private Class<? extends EditableParameterEntry> instanceClass;

    public CsvParameterEntrySerializer(Class<? extends EditableParameterEntry> instanceClass) {
        this.instanceClass = instanceClass;
    }

    private CsvPreference createCsvPreference(SerializationConfig config) {
        return new CsvPreference.Builder(config.getCsvQuote(), config.getCsvDelimiter(), config.getEndOfLine()).build();
    }

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

    @Override
    public void deserialize(SerializationConfig config, Reader reader, ParameterEntryPersister persister) throws SmartParamSerializationException {
        CsvListReader csvReader = new CsvListReader(reader, createCsvPreference(config));
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("started parameter entries deserialization at {}", startTime);

            // skip header
            csvReader.read();

            List<ParameterEntry> parameterEntries = new LinkedList<ParameterEntry>();
            // now read first data line
            List<String> line = csvReader.read();

            int readedLineCounter = 1;
            while (line != null) {
                parameterEntries.add(createParameterEntry(line));
                if (readedLineCounter % persister.batchSize() == 0) {
                    persist(persister, parameterEntries);
                }

                line = csvReader.read();
                readedLineCounter++;
            }

            if (!parameterEntries.isEmpty()) {
                persist(persister, parameterEntries);
            }

            long endTime = System.currentTimeMillis();
            logger.debug("deserializing {} parameter entries took {}", readedLineCounter, endTime - startTime);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("deserialization error", exception);
        } catch (ReflectiveOperationException reflectiveException) {
            throw new SmartParamSerializationException("error creating instance of " + instanceClass.getName() + ", maybe it has no default constructor?",
                    reflectiveException);
        } finally {
            closeReader(csvReader);
        }
    }

    private void persist(ParameterEntryPersister persister, List<ParameterEntry> parameterEntries) {
        persister.writeBatch(parameterEntries);
        parameterEntries.clear();
    }

    private ParameterEntry createParameterEntry(List<String> levelValues) throws ReflectiveOperationException {
        EditableParameterEntry parameterEntry = instanceClass.newInstance();
        parameterEntry.setLevels(levelValues.toArray(new String[levelValues.size()]));

        return parameterEntry;
    }

    private void closeReader(CsvListReader reader) throws SmartParamSerializationException {
        try {
            reader.close();
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while closing reader stream", exception);
        }
    }
}
