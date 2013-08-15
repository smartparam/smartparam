package org.smartparam.engine.config;

/**
 *
 * @author Adam Dubiel
 */
public interface ComponentInitializer {

    void initializeObject(Object configObject);

    boolean acceptsObject(Object configObject);
}
