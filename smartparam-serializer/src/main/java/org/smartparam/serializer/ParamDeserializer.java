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
public interface ParamDeserializer {

    Parameter deserialize(Reader reader) throws SmartParamSerializationException;

    Parameter deserializeConfig(BufferedReader reader) throws SmartParamSerializationException;

    void deserializeEntries(BufferedReader reader, ParameterEntryPersister persister) throws SmartParamSerializationException;

    SerializationConfig getSerializationConfig();
}
