package org.smartparam.serializer;

import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.engine.model.editable.EditableParameter;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.config.JsonParameterConfigDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamDeserializer extends RawSmartParamDeserializer {

    public StandardSmartParamDeserializer(
            SerializationConfig serializationConfig,
            Class<? extends EditableParameter> parameterInstanceClass,
            Class<? extends EditableLevel> levelInstanceClass,
            Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {

        super(serializationConfig,
                new JsonParameterConfigDeserializer(parameterInstanceClass, levelInstanceClass),
                new CsvParameterEntryDeserializer(parameterEntryInstanceClass));
    }
}
