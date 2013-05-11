
package org.smartparam.serializer.config;

import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.model.EditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterConfigSerializer {

    String serialize(Parameter parameter);

    <T extends EditableParameter> T deserialize(String configText, Class<T> implementingClass);
}
