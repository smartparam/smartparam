package org.smartparam.serializer;

import org.smartparam.serializer.config.JsonParameterConfigSerializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.engine.model.editable.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamSerializer extends RawSmartParamSerializer {

    public StandardSmartParamSerializer(
            SerializationConfig serializationConfig,
            Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {
        super(serializationConfig, new JsonParameterConfigSerializer(), new CsvParameterEntrySerializer());
    }
}
