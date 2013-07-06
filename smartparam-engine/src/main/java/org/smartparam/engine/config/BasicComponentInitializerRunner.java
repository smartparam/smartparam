package org.smartparam.engine.config;

import java.util.List;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BasicComponentInitializerRunner implements ComponentInitializerRunner {

    public List<ComponentInitializer> initializers;

    public BasicComponentInitializerRunner(List<ComponentInitializer> initializers) {
        this.initializers = initializers;
    }

    @Override
    public void runInitializersOnList(Iterable<?> objectsToInitialize) {
        for (Object objectToInitialize : objectsToInitialize) {
            runInitializers(objectToInitialize);
        }
    }

    @Override
    public void runInitializers(Object initializedObject) {
        for (ComponentInitializer initializer : initializers) {
            if (initializer.acceptsObject(initializedObject)) {
                initializer.initializeObject(initializedObject);
            }
        }
    }
}
