
package org.smartparam.serializer.config;

import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterConfigSerializer {

    String serialize(Parameter parameter);

    Parameter deserialize(String configText);
}
