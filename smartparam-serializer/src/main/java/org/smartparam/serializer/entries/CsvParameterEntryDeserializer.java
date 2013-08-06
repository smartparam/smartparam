package org.smartparam.serializer.entries;

import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

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
    public ParameterEntryBatchLoader deserialize(SerializationConfig config, BatchReaderWrapper readerWrapper) throws SmartParamSerializationException {
        return new CsvParameterEntryBatchLoader(instanceClass, config, readerWrapper);
    }
}
