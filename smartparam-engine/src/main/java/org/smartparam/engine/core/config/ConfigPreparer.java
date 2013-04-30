package org.smartparam.engine.core.config;

import java.lang.reflect.Field;
import java.util.Collections;
import org.smartparam.engine.core.exception.SmartParamConfigException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigPreparer {

    public SmartParamConfig getPreparedConfig(SmartParamConfig initialConfig) {
        SmartParamConfig preparedConfig = new SmartParamConfig();

        try {
            for (Field field : SmartParamConfig.class.getDeclaredFields()) {
                prepareField(field, initialConfig, preparedConfig);
            }
        } catch (SecurityException exception) {
            throw new SmartParamConfigException("exception occurred while preparing config", exception);
        }

        return preparedConfig;
    }

    private void prepareField(Field field, SmartParamConfig initialConfig, SmartParamConfig preparedConfig) {
        try {
            Object fieldValue = field.get(initialConfig);
            if (fieldValue == null) {
                fieldValue = prepareFieldValue(field);
            }

            field.set(preparedConfig, fieldValue);
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamConfigException("error while getting or setting field " + field.getName() + " value", exception);
        }
    }

    private Object prepareFieldValue(Field field) {
        if (field.isAnnotationPresent(ConfigDefault.class)) {
            ConfigDefault configDefault = field.getAnnotation(ConfigDefault.class);
            return createDefaultValue(configDefault);
        } else {
            throw new SmartParamConfigException("mandatory field " + field.getName() + " is null in provided configuration");
        }
    }

    private Object createDefaultValue(ConfigDefault configDefaultAnnotation) {
        if (configDefaultAnnotation.emptyMap()) {
            return Collections.emptyMap();
        }
        try {
            return configDefaultAnnotation.value().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamConfigException("unable to instantiate class " + configDefaultAnnotation.value().getCanonicalName(), exception);
        }
    }
}
