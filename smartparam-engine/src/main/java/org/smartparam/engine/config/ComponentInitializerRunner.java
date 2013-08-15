package org.smartparam.engine.config;

/**
 *
 * @author Adam Dubiel
 */
public interface ComponentInitializerRunner {

    void runInitializers(Object objectToInitialize);

    void runInitializersOnList(Iterable<?> objectsToInitialize);
}
