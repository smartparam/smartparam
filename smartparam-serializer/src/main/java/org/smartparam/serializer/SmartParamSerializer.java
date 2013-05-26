package org.smartparam.serializer;

import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.entries.ParameterEntrySupplier;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface SmartParamSerializer {

    public void serialize(SerializationConfig config, Parameter parameter, Writer writer) throws SmartParamSerializationException;

    public void serialize(SerializationConfig config, Parameter parameter, Writer writer, ParameterEntrySupplier supplier) throws SmartParamSerializationException;
}
