package org.smartparam.engine.config;

import com.google.common.collect.Sets;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.smartparam.engine.core.ItemsContainer;
import org.smartparam.engine.core.exception.SmartParamConfigException;
import org.smartparam.engine.util.BeanHelper;
import org.smartparam.engine.util.ReflectionsHelper;

/**
 * Builds (configures) object graph based on configuration object.
 *
 *
 * Algorithm:
 * <ol>
 * <li>
 * scan current object in search of writable properties (setters) that match
 * any field from configuration object
 * </li>
 * <li>
 * inject object from configuration and repeat algorithm on injected object
 * </li>
 * </ol>
 *
 * Algorithm traverses object graph depth-first. Any object can become a root.
 * Configuration object should have fields annotated using {@link ConfigElement}.
 * To inject collection, object hosting it has to be {@link ItemsContainer}.
 *
 * Configuration object can't have empty (null) fields.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigInjector {

    /**
     * Configuration object.
     */
    private SmartParamConfig config;

    /**
     * Simple map-cache of config object properties.
     */
    private Map<String, PropertyDescriptor> configProperties;

    /**
     * Associates collection to its owning object - association created based on
     * {@link ConfigElement#registerAt() } directive, put on collections in
     * config object.
     */
    private Map<Class<?>, Object> configCollections;

    /**
     * Config injector can operate only on one config.
     *
     * @param config configuration object for this instance of injector
     */
    public ConfigInjector(SmartParamConfig config) {
        this.config = config;
        initializeReflectionCache();
    }

    /**
     * Initializes local caches - configProperties and configCollections.
     */
    private void initializeReflectionCache() {
        try {
            configProperties = BeanHelper.getProperties(SmartParamConfig.class);
        } catch (IntrospectionException exception) {
            throw new SmartParamConfigException("unable to extract properties from object " + config, exception);
        }

        Set<Field> configElementFields = ReflectionsHelper.getFieldsAnnotatedBy(SmartParamConfig.class, ConfigElement.class);
        configCollections = new HashMap<Class<?>, Object>();

        try {
            Class<?> registerAtClass;
            for (Field field : configElementFields) {
                field.setAccessible(true);

                registerAtClass = field.getAnnotation(ConfigElement.class).registerAt();
                if (registerAtClass != ConfigElement.EMPTY.class) {
                    configCollections.put(registerAtClass, field.get(config));
                }
            }
        } catch (ReflectiveOperationException reflectiveException) {
            throw new SmartParamConfigException("unable to extract properies from " + config, reflectiveException);
        }

    }

    /**
     * Builds object graph starting from root object, based on configuration.
     *
     * @param rootObject object graph root
     */
    public void injectConfig(Object rootObject) {
        injectProperties(rootObject);
        injectIntoItemsContainer(rootObject);
    }

    /**
     * Scans object for properties matching any fields from configuration object,
     * injects them calling {@link #injectConfig(java.lang.Object) } recursively.
     * If target object has any {@link PostConstruct} methods, they are called
     * after all properties have been set (including collections).
     *
     * @param targetObject object to inject into
     */
    private void injectProperties(Object targetObject) {
        try {
            Map<String, PropertyDescriptor> objectProperties = BeanHelper.getProperties(targetObject.getClass());

            PropertyDescriptor propertyDescriptor, configPropertyDescriptor;
            Object propertyValue;
            for (String sharedProperty : Sets.intersection(objectProperties.keySet(), configProperties.keySet())) {
                propertyDescriptor = objectProperties.get(sharedProperty);
                configPropertyDescriptor = configProperties.get(sharedProperty);
                propertyValue = propertyDescriptor.getReadMethod().invoke(targetObject);

                if (propertyValue == null) {
                    propertyValue = injectPropertyFromConfig(targetObject, propertyDescriptor, config, configPropertyDescriptor);
                }
                injectConfig(propertyValue);
            }
            initializeIfPostConstructAvailable(targetObject);
        } catch (IntrospectionException exception) {
            throw new SmartParamConfigException("unable to extract properties from object " + targetObject, exception);
        } catch (ReflectiveOperationException reflectiveException) {
            throw new SmartParamConfigException("unable inject properties into object " + targetObject, reflectiveException);
        }
    }

    /**
     * Tries to match containerCandidate class with any collection holders from
     * {@link #configCollections}, if found and candidate is implementation of
     * {@link ItemsContainer}, items from collection are registered using
     * {@link ItemsContainer#setItems(java.util.Map) } function. After registration,
     * method iterates through collection items and tries to call {@link PostConstruct},
     * method, if any.
     *
     * @param itemsContainerCandidate probable holder of collection
     */
    @SuppressWarnings("unchecked")
    private void injectIntoItemsContainer(Object itemsContainerCandidate) {
        if (itemsContainerCandidate instanceof ItemsContainer) {
            Class<?> containerClass = itemsContainerCandidate.getClass();
            Object itemsObject = findConfigCollectionsEntry(containerClass);
            if (itemsObject != null) {
                ItemsContainer itemsContainer = (ItemsContainer) itemsContainerCandidate;
                itemsContainer.setItems((Map) itemsObject);

                for (Object object : itemsContainer.registeredItems().values()) {
                    initializeIfPostConstructAvailable(object);
                }
            }
        }
    }

    /**
     * Tries to match provided class with collection from {@link #configCollections}
     * cache.
     *
     * @param containerClass probable container class
     * @return collection that has to be registered in container class or null, if none
     */
    private Object findConfigCollectionsEntry(Class<?> containerClass) {
        for (Entry<Class<?>, Object> entry : configCollections.entrySet()) {
            if (entry.getKey().isAssignableFrom(containerClass)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Uses reflection magic to read property from config object and inject it
     * into target object.
     *
     * @param object target object
     * @param propertyDescriptor property descriptor from target object, has to have write method
     * @param config configuration object
     * @param configPropertyDescriptor property descriptor for config object, has to have read method
     * @return value of injected property
     *
     * @throws ReflectiveOperationException if method invocation fails
     */
    private Object injectPropertyFromConfig(Object object, PropertyDescriptor propertyDescriptor, SmartParamConfig config, PropertyDescriptor configPropertyDescriptor) throws ReflectiveOperationException {
        Object configPropertyValue = configPropertyDescriptor.getReadMethod().invoke(config);
        propertyDescriptor.getWriteMethod().invoke(object, configPropertyValue);

        return configPropertyValue;
    }

    /**
     * Scans class in search of {@link PostConstruct} methods and invokes them,
     * no order is guaranteed.
     *
     * @param object object to scan
     */
    private void initializeIfPostConstructAvailable(Object object) {
        try {
            for (Method method : ReflectionsHelper.getMethodsAnnotatedBy(object.getClass(), PostConstruct.class)) {
                method.invoke(object);
            }
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamConfigException("error while invoking @PostConstruct method", exception);
        }
    }
}
