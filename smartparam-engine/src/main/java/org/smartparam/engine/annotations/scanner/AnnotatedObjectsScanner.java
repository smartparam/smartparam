package org.smartparam.engine.annotations.scanner;

import org.smartparam.engine.bean.RepositoryObjectKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamSortable;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.AnnotationScanner;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;
import org.smartparam.engine.util.reflection.ReflectionsScanner;

/**
 * Annotation scanner util supporting SmartParam* annotations. It not only scans
 * for annotated objects, but also instantiates them using hints included in
 * annotation. Annotation should define:
 * <ul>
 * <li>value() - to extract unique code of object</li>
 * <li>values() - if same object should be returned multiple times under different names</li>
 * <li>instances() - if object should be instantiated with specific constructor args</li>
 * </ul>
 * Also, if scanned annotation is {@link SmartParamSortable}, it should define
 * order() method.
 *
 * @see org.smartparam.engine.annotations.SmartParamMatcher
 * @see SmartParamObjectInstance
 *
 * Map returned by {@link #getAnnotatedObjects(java.lang.Class) } is not ordered,
 * but the key ({@link RepositoryObjectKey}) holds all information that can be
 * used for ordering.
 *
 * If instantiated object implements {@link AnnotationScanner} interface,
 * its scanner properties are set after instantiation using
 * {@link AnnotationScanner#setScannerProperties(org.smartparam.engine.bean.AnnotationScannerProperties) }.
 *
 *
 * @param <OBJECT> type of object to instantiate
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedObjectsScanner<OBJECT> extends AbstractAnnotationScanner {

    /**
     * For all {@link SmartParamSortable}, reasonable default value for order()
     * method.
     */
    public static final int DEFAULT_ORDER_VALUE = 100;

    /**
     * Name of annotation method holding array of {@link SmartParamObjectInstance}
     * instance descriptors.
     */
    private static final String INSTANCES_METHOD_NAME = "instances";

    /**
     * Name of annotation method holding array of names, that will be used to
     * register same object multiple times.
     */
    private static final String VALUES_METHOD_NAME = "values";

    /**
     * Name of annotation method holding order information.
     */
    private static final String ORDER_METHOD_NAME = "order";

    /**
     * In case instantiated object is {@link AnnotationScanner}, this method will
     * be used to inject scanner properties.
     */
    private static final String SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME = "setScannerProperties";

    /**
     * List of packages to scan.
     */
    private PackageList packagesToScan;

    /**
     * Properties object that will be injected of instantiated object is
     * {@link AnnotationScanner}.
     */
    private AnnotationScannerProperties propertiesToInject;

    private ReflectionsScanner reflectionsScanner = new ReflectionsScanner();

    /**
     * Constructor that takes list of packages to scan, same list of packages
     * will be injected to objects if they happen to be {@link AnnotationScanner}.
     *
     * @param packagesToScan list of packages to scan
     */
    public AnnotatedObjectsScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
        this.propertiesToInject = new AnnotationScannerProperties();
        propertiesToInject.setPackagesToScan(packagesToScan);
    }

    /**
     * Scan for objects annotated with given annotation type instantiate them
     * using hints from annotation and return a map of those objects.
     *
     * Might throw {@link SmartParamInitializationException} if any reflective
     * operation fails.
     *
     * @see RepositoryObjectKey
     * @param annotationClass annotation to look for
     *
     * @return map of objects
     */
    public Map<RepositoryObjectKey, OBJECT> getAnnotatedObjects(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> annotatedObjectClasses = reflectionsScanner.findClassesAnnotatedWith(annotationClass, packagesToScan.getPackages());
        Map<RepositoryObjectKey, OBJECT> objects = instantiateObjectsFromAnotations(annotatedObjectClasses, annotationClass);

        return objects;
    }

    /**
     * Find all types annotated with given annotation and instantiate them.
     *
     * @param annotatedObjectClasses types to instantiate
     * @param annotationClass        searched annotation
     *
     * @return map of instantiated objects
     */
    private Map<RepositoryObjectKey, OBJECT> instantiateObjectsFromAnotations(Set<Class<?>> annotatedObjectClasses, Class<? extends Annotation> annotationClass) {
        Map<RepositoryObjectKey, OBJECT> types = new HashMap<RepositoryObjectKey, OBJECT>();

        Annotation typeAnnotation;
        for (Class<?> type : annotatedObjectClasses) {
            typeAnnotation = type.getAnnotation(annotationClass);
            types.putAll(interpreteAnnotation(type, typeAnnotation));
        }

        return types;
    }

    /**
     * Create one or more objects of given class, that were annotated using
     * annotation instance. Size of returned map (number of created objects)
     * depend on annotation options.
     *
     * @param objectClass objects of this class will be created
     * @param annotation  annotation that controls instantiation process
     *
     * @return map of all created objects
     */
    private Map<RepositoryObjectKey, OBJECT> interpreteAnnotation(Class<?> objectClass, Annotation annotation) {
        Map<RepositoryObjectKey, OBJECT> instantiatedObjects = new HashMap<RepositoryObjectKey, OBJECT>();
        SmartParamObjectInstance[] instanceDescriptors = extractInstanceDescriptors(annotation);
        int scannedObjectOrder = extractOrder(annotation);

        OBJECT object;
        if (instanceDescriptors.length == 0) {
            String[] objectIdentifiers = extractIdentifiers(annotation);
            object = instantiateUsingDefaultConstructor(objectClass);

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

    /**
     * Create new object of given class using its default constructor. If class
     * implements {@link AnnotationScanner}, scanner properties are injected.
     *
     * Will throw {@link SmartParamInitializationException} if there is no
     * default constructor or it is unreachable.
     *
     * @param objectClass class of object to create
     *
     * @return created object
     */
    @SuppressWarnings("unchecked")
    private OBJECT instantiateUsingDefaultConstructor(Class<?> objectClass) {
        try {
            OBJECT object = (OBJECT) objectClass.newInstance();
            injectPackagesToScanForScanners(objectClass, object);
            return object;
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no default constructor "
                    + "found for class " + objectClass.getCanonicalName());
        }
    }

    /**
     * Create object instance using instructions from object descriptor.
     *
     * Will throw {@link SmartParamInitializationException} if there is no
     * constructor matching descriptor.
     *
     * @see SmartParamObjectInstance
     *
     * @param objectClass      class to instantiate
     * @param objectDescriptor object instance descriptor
     *
     * @return created object
     */
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
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no String[" + constructorArgCount + "] constructor "
                    + "found for class " + objectClass.getCanonicalName());
        }
    }

    /**
     * Tries to inject scanner properties into object if it is instance of
     * {@link AnnotationScanner}.
     *
     * Uses method named
     * {@link #SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME}.
     *
     * Will throw {@link SmartParamInitializationException} if any reflective
     * error occurs.
     *
     * @param objectClass class of object
     * @param object      object to inject properties into
     */
    private void injectPackagesToScanForScanners(Class<?> objectClass, OBJECT object) {
        try {
            if (objectClass.isInstance(AnnotationScanner.class)) {
                objectClass.getMethod(SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME, AnnotationScannerProperties.class).invoke(object, propertiesToInject);
            }
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no "
                    + SET_ANNOTATION_SCANNER_PROPERTIES_METHOD_NAME + " method found for class "
                    + objectClass.getCanonicalName());
        }
    }

    /**
     * Tries to extract order defined in annotation (in order() method),
     * returns default order {@link #DEFAULT_ORDER_VALUE} if annotation is not
     * {@link SmartParamSortable}.
     *
     * @param annotation annotation instance to extract order form
     *
     * @return order read from annotation or default value
     */
    private int extractOrder(Annotation annotation) {
        if (annotation.annotationType().isAnnotationPresent(SmartParamSortable.class)) {
            return (Integer) extractValue(annotation, ORDER_METHOD_NAME);
        }
        return DEFAULT_ORDER_VALUE;
    }

    /**
     * Returns array of identifiers defined in values() method (might be empty).
     *
     * @param annotation annotation instance to extract identifiers from
     *
     * @return array of names (identifiers), never null
     */
    protected String[] extractIdentifiers(Annotation annotation) {
        String[] identifiers = (String[]) extractValue(annotation, VALUES_METHOD_NAME);
        if (identifiers.length > 0) {
            return identifiers;
        }
        return new String[]{extractValue(annotation)};
    }

    /**
     * Return array of defined instance descriptors (might be empty).
     *
     * @param annotation annotation instance
     *
     * @return array of descriptors, never null
     */
    private SmartParamObjectInstance[] extractInstanceDescriptors(Annotation annotation) {
        try {
            Method instanceDescriptorsMethod = annotation.annotationType().getMethod(INSTANCES_METHOD_NAME);
            return (SmartParamObjectInstance[]) instanceDescriptorsMethod.invoke(annotation);
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + INSTANCES_METHOD_NAME + " field found on annotation " + annotation.annotationType().getSimpleName());
        }
    }

    public void setReflectionsScanner(ReflectionsScanner reflectionsScanner) {
        this.reflectionsScanner = reflectionsScanner;
    }
}
