package org.smartparam.serializer;

import java.io.BufferedReader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamDeserializer {

    Parameter deserialize(BufferedReader reader) throws SmartParamSerializationException;

    Parameter deserializeConfig(BufferedReader reader) throws SmartParamSerializationException;

    ParameterEntryBatchLoader deserializeEntries(BufferedReader reader) throws SmartParamSerializationException;

    SerializationConfig getSerializationConfig();
}
