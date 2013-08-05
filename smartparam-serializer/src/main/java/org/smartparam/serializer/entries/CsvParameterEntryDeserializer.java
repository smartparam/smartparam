package org.smartparam.serializer.entries;

import java.io.BufferedReader;
import java.io.IOException;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryDeserializer implements ParameterEntryDeserializer {

    private Class<? extends EditableParameterEntry> instanceClass;

    public CsvParameterEntryDeserializer(Class<? extends EditableParameterEntry> instanceClass) {
        this.instanceClass = instanceClass;
    }

    @Override
    public ParameterEntryBatchLoader deserialize(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException {
        CsvListReader csvReader = new CsvListReader(reader, CsvPreferenceBuilder.csvPreference(config));
        try {
            // drop header
            csvReader.read();
        } catch (IOException exception) {
            throw new SmartParamSerializationException("failed to read from csv reader", exception);
        }

        return new CsvParameterEntryBatchLoader(instanceClass, csvReader);
    }
}
