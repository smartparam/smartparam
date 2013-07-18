package org.smartparam.serializer.entries;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryDeserializer extends AbstractCsvParameterEntrySerializer implements ParameterEntryDeserializer {

    private static final Logger logger = LoggerFactory.getLogger(CsvParameterEntrySerializer.class);

    private Class<? extends EditableParameterEntry> instanceClass;

    public CsvParameterEntryDeserializer(Class<? extends EditableParameterEntry> instanceClass) {
        this.instanceClass = instanceClass;
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
        } catch (IllegalAccessException illegalAccessException) {
            throw new SmartParamSerializationException("error creating instance of " + instanceClass.getName() + ", maybe it has no default constructor?",
                    illegalAccessException);
        } catch (InstantiationException instantiationException) {
            throw new SmartParamSerializationException("error creating instance of " + instanceClass.getName() + ", maybe it has no default constructor?",
                    instantiationException);
        } finally {
            closeReader(csvReader);
        }
    }

    private void persist(ParameterEntryPersister persister, List<ParameterEntry> parameterEntries) {
        persister.writeBatch(parameterEntries);
        parameterEntries.clear();
    }

    private ParameterEntry createParameterEntry(List<String> levelValues) throws IllegalAccessException, InstantiationException {
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
