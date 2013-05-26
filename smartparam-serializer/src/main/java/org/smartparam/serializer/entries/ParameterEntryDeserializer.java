package org.smartparam.serializer.entries;

import java.io.Reader;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntryDeserializer {

    void deserialize(SerializationConfig config, Reader reader, ParameterEntryPersister persister) throws SmartParamSerializationException;
}
