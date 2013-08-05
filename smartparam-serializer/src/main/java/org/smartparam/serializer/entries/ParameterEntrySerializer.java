package org.smartparam.serializer.entries;

import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntrySerializer {

    void serialize(SerializationConfig config, Writer writer, Parameter parameter, ParameterEntryBatchLoader parameterEntryLoader) throws SmartParamSerializationException;
}
