package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @param <OBJECT> 
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedObjectsScanner<OBJECT> extends AbstractAnnotationScanner {

    private final static String INSTANCES_METHOD_NAME = "instances";

    private final static String VALUES_METHOD_NAME = "values";

    public Map<String, OBJECT> getAnnotatedObjects(PackageList packagesToScan, Class<? extends Annotation> annotationClass) {
        Map<String, OBJECT> objects = instantiateObjectsFromAnotations(getReflectionsForPackages(packagesToScan), annotationClass);

        return objects;
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

        OBJECT object;
        if (instanceDescriptors.length == 0) {
            String[] objectIdentifiers = extractIdentifiers(annotation);
            object = instantiateWithDefault(objectClass);

            for (String objectIdentifier : objectIdentifiers) {
                instantiatedObjects.put(objectIdentifier, object);
            }
        } else {
            String objectIdentifier;
            for (SmartParamObjectInstance instanceDescriptor : instanceDescriptors) {
                objectIdentifier = extractValue(instanceDescriptor);
                object = instantiateUsingObjectDescriptor(objectClass, instanceDescriptor);
                instantiatedObjects.put(objectIdentifier, object);
            }
        }

        return instantiatedObjects;
    }

    @SuppressWarnings("unchecked")
    private OBJECT instantiateWithDefault(Class<?> objectClass) throws SmartParamException {
        try {
            return (OBJECT) objectClass.newInstance();
        } catch (Exception exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no default constructor "
                    + "found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }

    @SuppressWarnings("unchecked")
    private OBJECT instantiateUsingObjectDescriptor(Class<?> objectClass, SmartParamObjectInstance objectDescriptor) {
        int constructorArgCount = objectDescriptor.constructorArgs().length;
        try {
            Class<?>[] constructorArgClasses = new Class<?>[constructorArgCount];
            Object[] constructorArgs = new Object[constructorArgCount];

            String constructorArg;
            for (int i = 0; i < constructorArgCount; ++i) {
                constructorArg = objectDescriptor.constructorArgs()[i];
                constructorArgClasses[i] = constructorArg.getClass();
                constructorArgs[i] = constructorArg;
            }

            return (OBJECT) objectClass.getConstructor(constructorArgClasses).newInstance(constructorArgs);
        } catch (Exception exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no String[" + constructorArgCount + "] constructor "
                    + "found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }

    protected String[] extractIdentifiers(Annotation annotation) {
        String[] identifiers = (String[]) extractValue(annotation, VALUES_METHOD_NAME);
        if (identifiers.length > 0) {
            return identifiers;
        }
        return new String[]{extractValue(annotation)};
    }

    private SmartParamObjectInstance[] extractInstanceDescriptors(Annotation annotation) {
        try {
            Method instanceDescriptorsMethod = annotation.annotationType().getMethod(INSTANCES_METHOD_NAME);
            return (SmartParamObjectInstance[]) instanceDescriptorsMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + INSTANCES_METHOD_NAME + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }
}
