package org.smartparam.engine.config;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ComponentInitializer {

    void initializeObject(ComponentInitializerRunner initializerRunner, Object configObject);

    boolean acceptsObject(Object configObject);
}
