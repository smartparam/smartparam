package org.smartparam.serializer;

import org.smartparam.serializer.config.JsonParameterConfigSerializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.mgmt.model.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamSerializer extends RawSmartParamSerializer {

    public StandardSmartParamSerializer(Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {

        super(new JsonParameterConfigSerializer(), new CsvParameterEntrySerializer());
    }
}
