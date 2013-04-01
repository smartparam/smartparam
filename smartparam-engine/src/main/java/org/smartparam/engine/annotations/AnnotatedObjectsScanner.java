package org.smartparam.engine.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.SmartParamConsts;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedObjectsScanner<OBJECT> implements SmartParamConsts {

    private final static String VALUE_METHOD_NAME = "value";

    private final static String INSTANCES_METHOD_NAME = "instances";

    public Map<String, OBJECT> getAnnotatedObjects(PackageList packagesToScan, Class<? extends Annotation> annotationClass) {
        Map<String, OBJECT> objects = instantiateObjectsFromAnotations(getReflectionsForDefaultPackage(), annotationClass);
        Map<String, OBJECT> userObjects = instantiateObjectsFromAnotations(getReflectionsForPackages(packagesToScan), annotationClass);

        // override defaults
        objects.putAll(userObjects);

        return objects;
    }

    private Reflections getReflectionsForPackages(PackageList packageList) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        for (String packageStem : packageList) {
            builder.addUrls(ClasspathHelper.forPackage(packageStem));
        }

        return builder.build();
    }

    private Reflections getReflectionsForDefaultPackage() {
        return new Reflections(BASE_PACKAGE_PREFIX);
    }

    private Map<String, OBJECT> instantiateObjectsFromAnotations(Reflections reflections, Class<? extends Annotation> annotationClass) {
        Map<String, OBJECT> types = new HashMap<String, OBJECT>();

        Annotation typeAnnotation;
        for (Class<?> type : reflections.getTypesAnnotatedWith(annotationClass)) {
            typeAnnotation = type.getAnnotation(annotationClass);
            types.putAll(interpreteAnnotation(type, typeAnnotation));
        }

        return types;
    }

    private Map<String, OBJECT> interpreteAnnotation(Class<?> objectClass, Annotation annotation) {
        Map<String, OBJECT> instantiatedObjects = new HashMap<String, OBJECT>();
        SmartParamObjectInstance[] instanceDescriptors = extractInstanceDescriptors(annotation);

        String objectIdentifier;
        OBJECT object;
        if (instanceDescriptors.length == 0) {
            objectIdentifier = extractObjectIdentifier(annotation);
            object = instantiateWithDefault(objectClass);
            instantiatedObjects.put(objectIdentifier, object);
        } else {
            for (SmartParamObjectInstance instanceDescriptor : instanceDescriptors) {
                objectIdentifier = extractObjectIdentifier(instanceDescriptor);
                object = instantiateUsingObjectDescriptor(objectClass, instanceDescriptor);
                instantiatedObjects.put(objectIdentifier, object);
            }
        }

        return instantiatedObjects;
    }

    private OBJECT instantiateWithDefault(Class<?> objectClass) throws ParamException {
        try {
            return (OBJECT) objectClass.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new ParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no default constructor "
                    + "found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }

    private OBJECT instantiateUsingObjectDescriptor(Class<?> objectClass, SmartParamObjectInstance objectDescriptor) {
        int constructorArgCount = objectDescriptor.constructorArgs().length;
        try {
            Class<?>[] constructorArgClasses = new Class<?>[constructorArgCount];
            Object[] constructorArgs = new Object[constructorArgCount];

            String constructorArg;
            for(int i = 0; i < constructorArgCount; ++i) {
                constructorArg = objectDescriptor.constructorArgs()[i];
                constructorArgClasses[i] = constructorArg.getClass();
                constructorArgs[i] = constructorArg;
            }

            return (OBJECT) objectClass.getConstructor(constructorArgClasses).newInstance(constructorArgs);
        } catch (Exception exception) {
            throw new ParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no String[" + constructorArgCount + "] constructor "
                    + "found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }

    private SmartParamObjectInstance[] extractInstanceDescriptors(Annotation annotation) {
        try {
            Method instanceDescriptorsMethod = annotation.annotationType().getMethod(INSTANCES_METHOD_NAME);
            return (SmartParamObjectInstance[]) instanceDescriptorsMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + VALUE_METHOD_NAME + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }

    private String extractObjectIdentifier(Annotation annotation) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(VALUE_METHOD_NAME);
            return (String) defaultValueMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + VALUE_METHOD_NAME + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }
}
