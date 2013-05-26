package org.smartparam.serializer;

import org.smartparam.mgmt.model.EditableLevel;
import org.smartparam.mgmt.model.EditableParameter;
import org.smartparam.mgmt.model.EditableParameterEntry;
import org.smartparam.serializer.config.JsonParameterConfigDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamDeserializer extends RawSmartParamDeserializer {

    public StandardSmartParamDeserializer(Class<? extends EditableParameter> parameterInstanceClass,
            Class<? extends EditableLevel> levelInstanceClass, Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {

        super(new JsonParameterConfigDeserializer(parameterInstanceClass, levelInstanceClass),
                new CsvParameterEntryDeserializer(parameterEntryInstanceClass));
    }
}
