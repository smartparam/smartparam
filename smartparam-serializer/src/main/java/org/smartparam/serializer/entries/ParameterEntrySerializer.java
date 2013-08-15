package org.smartparam.serializer.entries;

import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public interface ParameterEntrySerializer {

    void serialize(SerializationConfig config, Writer writer, Parameter parameter, ParameterEntryBatchLoader parameterEntryLoader) throws SmartParamSerializationException;
}
