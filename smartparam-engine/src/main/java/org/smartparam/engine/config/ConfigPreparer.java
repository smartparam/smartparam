package org.smartparam.engine.config;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.exception.SmartParamConfigException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public abstract class ConfigPreparer<CONFIG_OBJECT> {

    public CONFIG_OBJECT getPreparedConfig(CONFIG_OBJECT initialConfig) {
        CONFIG_OBJECT preparedObject = createInstance(initialConfig);

        try {
            for (Field field : initialConfig.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                prepareField(field, initialConfig, preparedObject);
            }
        } catch (SecurityException exception) {
            throw new SmartParamConfigException("exception occurred while preparing config", exception);
        }

        return preparedObject;
    }

    protected abstract CONFIG_OBJECT createInstance(CONFIG_OBJECT initialConfig);

    protected abstract void customizeNewDefaultValue(CONFIG_OBJECT initialConfigObject, Object object);

    private void prepareField(Field field, CONFIG_OBJECT initialConfigObject, CONFIG_OBJECT preparedConfigObject) {
        try {
            Object fieldValue = field.get(initialConfigObject);
            if (fieldValue == null) {
                fieldValue = prepareFieldValue(field, initialConfigObject);
            }

            field.set(preparedConfigObject, fieldValue);
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamConfigException("error while getting or setting field " + field.getName() + " value", exception);
        }
    }

    private Object prepareFieldValue(Field field, CONFIG_OBJECT initialConfigObject) {
        if (field.isAnnotationPresent(ConfigElement.class)) {
            ConfigElement configDefault = field.getAnnotation(ConfigElement.class);
            return createDefaultValue(configDefault, initialConfigObject);
        } else {
            throw new SmartParamConfigException("mandatory field " + field.getName() + " is null in provided configuration");
        }
    }

    private Object createDefaultValue(ConfigElement configElementAnnotation, CONFIG_OBJECT initialConfigObject) {
        Class<?> defaultObjectClass = configElementAnnotation.value();

        if (defaultObjectClass.isAssignableFrom(Map.class)) {
            return Collections.emptyMap();
        }
        else if(defaultObjectClass.isAssignableFrom(List.class)) {
            return Collections.emptyList();
        }
        try {
            Object defaultValue = configElementAnnotation.value().newInstance();
            customizeNewDefaultValue(initialConfigObject, defaultValue);
            return defaultValue;
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamConfigException("unable to instantiate class " + configElementAnnotation.value().getCanonicalName(), exception);
        }
    }
}
