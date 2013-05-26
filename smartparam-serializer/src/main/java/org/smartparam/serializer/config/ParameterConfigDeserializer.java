package org.smartparam.serializer.config;

import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterConfigDeserializer {

    Parameter deserialize(String configText);
}
