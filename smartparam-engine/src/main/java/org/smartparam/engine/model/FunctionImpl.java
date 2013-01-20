package org.smartparam.engine.model;

/**
 * Function implementation.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface FunctionImpl {

    /**
     * Returns type of implementation - links implementation with function provider.
     *
     * @return type of implementation
     */
    String getTypeCode();
}
