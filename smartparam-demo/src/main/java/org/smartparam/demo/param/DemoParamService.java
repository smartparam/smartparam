package org.smartparam.demo.param;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Link between demo application and SmartParam engine.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface DemoParamService {

    AbstractHolder get(String paramName, DemoParamContext context);

}
