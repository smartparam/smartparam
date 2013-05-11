package org.smartparam.serializer.entries;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import org.smartparam.serializer.exception.SmartParamSerializerException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntrySerializer {

    void serialize(Writer writer, List<String> header, ParameterEntrySupplier supplier) throws SmartParamSerializerException;

    void deserialize(Reader reader, ParameterEntryPersister persister) throws SmartParamSerializerException;
}
