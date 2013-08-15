package org.smartparam.serializer.config;

import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public interface ParameterConfigSerializer {

    String serialize(Parameter parameter);
}
