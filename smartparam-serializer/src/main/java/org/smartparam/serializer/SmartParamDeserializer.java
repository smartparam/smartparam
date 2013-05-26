package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.Reader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.entries.ParameterEntryPersister;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface SmartParamDeserializer {

    public Parameter deserialize(SerializationConfig config, Reader reader) throws SmartParamSerializationException;

    public Parameter deserializeConfig(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException;

    public void deserializeEntries(SerializationConfig config, BufferedReader reader, ParameterEntryPersister persister) throws SmartParamSerializationException;
}
