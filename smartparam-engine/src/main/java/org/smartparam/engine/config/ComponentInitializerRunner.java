package org.smartparam.engine.config;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ComponentInitializerRunner {

    void runInitializers(Object objectToInitialize);

    void runInitializersOnList(Iterable<?> objectsToInitialize);
}
