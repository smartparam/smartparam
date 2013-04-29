package org.smartparam.engine.annotations.scanner;

import org.smartparam.engine.bean.RepositoryObjectKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamSortable;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.AnnotationScanner;
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

    public static final int DEFAULT_ORDER_VALUE = 100;

    private final static String INSTANCES_METHOD_NAME = "instances";

    private final static String VALUES_METHOD_NAME = "values";

    private final static String ORDER_METHOD_NAME = "order";

    private final static String SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME = "setScannerProperties";

    private PackageList packagesToScan;

    private AnnotationScannerProperties propertiesToInject;

    public AnnotatedObjectsScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
        this.propertiesToInject = new AnnotationScannerProperties();
        propertiesToInject.setPackagesToScan(packagesToScan);
    }

    public Map<RepositoryObjectKey, OBJECT> getAnnotatedObjects(Class<? extends Annotation> annotationClass) {
        Map<RepositoryObjectKey, OBJECT> objects = instantiateObjectsFromAnotations(getReflectionsForPackages(packagesToScan), annotationClass);

        return objects;
    }

    private Map<RepositoryObjectKey, OBJECT> instantiateObjectsFromAnotations(Reflections reflections, Class<? extends Annotation> annotationClass) {
        Map<RepositoryObjectKey, OBJECT> types = new HashMap<RepositoryObjectKey, OBJECT>();

        Annotation typeAnnotation;
        for (Class<?> type : reflections.getTypesAnnotatedWith(annotationClass)) {
            typeAnnotation = type.getAnnotation(annotationClass);
            types.putAll(interpreteAnnotation(type, typeAnnotation));
        }

        return types;
    }

    private Map<RepositoryObjectKey, OBJECT> interpreteAnnotation(Class<?> objectClass, Annotation annotation) {
        Map<RepositoryObjectKey, OBJECT> instantiatedObjects = new HashMap<RepositoryObjectKey, OBJECT>();
        SmartParamObjectInstance[] instanceDescriptors = extractInstanceDescriptors(annotation);
        int scannedObjectOrder = extractOrder(annotation);

        OBJECT object;
        if (instanceDescriptors.length == 0) {
            String[] objectIdentifiers = extractIdentifiers(annotation);
            object = instantiateWithDefault(objectClass);

            for (String objectIdentifier : objectIdentifiers) {
                instantiatedObjects.put(new RepositoryObjectKey(objectIdentifier, scannedObjectOrder), object);
            }
        } else {
            String objectIdentifier;
            for (SmartParamObjectInstance instanceDescriptor : instanceDescriptors) {
                objectIdentifier = extractValue(instanceDescriptor);
                object = instantiateUsingObjectDescriptor(objectClass, instanceDescriptor);
                instantiatedObjects.put(new RepositoryObjectKey(objectIdentifier, scannedObjectOrder), object);
            }
        }

        return instantiatedObjects;
    }

    @SuppressWarnings("unchecked")
    private OBJECT instantiateWithDefault(Class<?> objectClass) throws SmartParamException {
        try {
            OBJECT object = (OBJECT) objectClass.newInstance();
            injectPackagesToScanForScanners(objectClass, object);
            return object;
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

            OBJECT object = (OBJECT) objectClass.getConstructor(constructorArgClasses).newInstance(constructorArgs);
            injectPackagesToScanForScanners(objectClass, object);
            return object;
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no String[" + constructorArgCount + "] constructor "
                    + "found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }

    private void injectPackagesToScanForScanners(Class<?> objectClass, OBJECT object) {
        try {
            if (objectClass.isInstance(AnnotationScanner.class)) {
                objectClass.getMethod(SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME, AnnotationScannerProperties.class).invoke(object, propertiesToInject);
            }
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no "
                    + SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME + " method found for class "
                    + ClassUtils.getShortClassName(objectClass));
        }
    }

    private int extractOrder(Annotation annotation) {
        if (annotation.annotationType().isAnnotationPresent(SmartParamSortable.class)) {
            return (Integer) extractValue(annotation, ORDER_METHOD_NAME);
        }
        return DEFAULT_ORDER_VALUE;
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
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + INSTANCES_METHOD_NAME + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }
}
