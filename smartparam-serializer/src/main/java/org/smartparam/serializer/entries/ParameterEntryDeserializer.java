package org.smartparam.serializer.entries;

import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import java.io.BufferedReader;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntryDeserializer {

    public ParameterEntryBatchLoader deserialize(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException;
}
