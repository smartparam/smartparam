package org.smartparam.engine.annotations;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.smartparam.engine.annotations.handler.AnnotationHandler;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.SmartParamConsts;
import static org.smartparam.engine.bean.SmartParamConsts.BASE_PACKAGE_PREFIX;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedObjectsScanner implements SmartParamConsts {

    public <OBJECT> Map<String, OBJECT> getAnnotatedObjects(PackageList packagesToScan, AnnotationHandler annotationHandler, Class<OBJECT> objectClass) {
        Map<String, Class<?>> objects = lookupAnnotatedClasses(getReflectionsForDefaultPackage(), annotationHandler);
        Map<String, Class<?>> userObjects = lookupAnnotatedClasses(getReflectionsForPackages(packagesToScan), annotationHandler);

        // override defaults
        objects.putAll(userObjects);

        Map<String, OBJECT> instantiatedObjects = new HashMap<String, OBJECT>();
        for(Entry<String, Class<?>> entry : objects.entrySet()) {
            OBJECT type = (OBJECT) instantiate(entry.getValue());
            instantiatedObjects.put(entry.getKey(), type);
        }

        return instantiatedObjects;
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

    private Map<String, Class<?>> lookupAnnotatedClasses(Reflections reflections, AnnotationHandler handler) {
        Map<String, Class<?>> types = new HashMap<String, Class<?>>();

        Annotation typeAnnotation;
        for (Class<?> type : reflections.getTypesAnnotatedWith(handler.handledAnnotation())) {
            typeAnnotation = type.getAnnotation(handler.handledAnnotation());
            types.put(handler.extractIdentifier(typeAnnotation), type);
        }

        return types;
    }

    private Object instantiate(Class<?> objectClass) throws ParamException {
        try {
            return objectClass.getConstructor().newInstance();
        }
        catch(Exception exception) {
            throw new ParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "no default constructor found for class " + ClassUtils.getShortClassName(objectClass));
        }
    }
}
