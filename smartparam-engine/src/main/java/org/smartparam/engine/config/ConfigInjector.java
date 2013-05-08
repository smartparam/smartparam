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
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigInjector {

    private SmartParamConfig config;

    private Map<String, PropertyDescriptor> configProperties;

    private Map<Class<?>, Object> configCollections;

    public ConfigInjector(SmartParamConfig config) {
        this.config = config;
        initializeReflectionCache();
    }

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

    public void injectConfig(Object rootObject) {
        injectProperties(rootObject);
        injectIntoItemsContainer(rootObject);
    }

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

    @SuppressWarnings("unchecked")
    private void injectIntoItemsContainer(Object itemsContainerCandidate) {
        Class<?> containerClass = itemsContainerCandidate.getClass();
        Object itemsObject = findConfigCollectionsEntry(containerClass);
        if(itemsContainerCandidate instanceof ItemsContainer && itemsObject != null) {
            ItemsContainer itemsContainer = (ItemsContainer) itemsContainerCandidate;
            itemsContainer.setItems( (Map) itemsObject );

            for(Object object : itemsContainer.registeredItems().values()) {
                initializeIfPostConstructAvailable(object);
            }
        }
    }

    private Object findConfigCollectionsEntry(Class<?> containerClass) {
        for(Entry<Class<?>, Object> entry : configCollections.entrySet()) {
            if(entry.getKey().isAssignableFrom(containerClass)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private Object injectPropertyFromConfig(Object object, PropertyDescriptor propertyDescriptor, SmartParamConfig config, PropertyDescriptor configPropertyDescriptor) throws ReflectiveOperationException {
        Object configPropertyValue = configPropertyDescriptor.getReadMethod().invoke(config);
        propertyDescriptor.getWriteMethod().invoke(object, configPropertyValue);

        return configPropertyValue;
    }

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
