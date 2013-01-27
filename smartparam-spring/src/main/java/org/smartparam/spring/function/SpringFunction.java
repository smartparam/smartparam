package org.smartparam.spring.function;

import org.smartparam.engine.model.FunctionImpl;

/**
 * Function repository type for plugins stored inside spring beans.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface SpringFunction extends FunctionImpl {

    /**
     * Returns spring bean identifier which contains plugin method.
     *
     * @return spring bean name
     */
    String getBeanName();

    /**
     * Name of method to run.
     *
     * @return method name
     */
    String getMethodName();
}
