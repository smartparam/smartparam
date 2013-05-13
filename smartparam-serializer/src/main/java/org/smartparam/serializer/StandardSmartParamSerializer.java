package org.smartparam.serializer;

import org.smartparam.serializer.config.JsonParameterConfigSerializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.serializer.model.EditableLevel;
import org.smartparam.serializer.model.EditableParameter;
import org.smartparam.serializer.model.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamSerializer extends RawSmartParamSerializer {

    public StandardSmartParamSerializer(Class<? extends EditableParameter> parameterInstanceClass, 
            Class<? extends EditableLevel> levelInstanceClass, Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {
        
        super(new JsonParameterConfigSerializer(parameterInstanceClass, levelInstanceClass),
                new CsvParameterEntrySerializer(parameterEntryInstanceClass));
    }
}
