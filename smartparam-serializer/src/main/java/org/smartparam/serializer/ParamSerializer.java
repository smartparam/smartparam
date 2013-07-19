package org.smartparam.serializer;

import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.entries.ParameterEntrySupplier;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamSerializer {

    public void serialize(Parameter parameter, Writer writer) throws SmartParamSerializationException;

    public void serialize(Parameter parameter, Writer writer, ParameterEntrySupplier supplier) throws SmartParamSerializationException;

    SerializationConfig getSerializationConfig();
}
