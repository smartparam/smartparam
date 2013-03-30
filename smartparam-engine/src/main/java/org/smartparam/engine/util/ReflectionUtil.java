package org.smartparam.engine.util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.smartparam.engine.annotations.handler.AnnotationHandler;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.SmartParamConsts;

/**
 *
 * @author Adam Dubiel
 */
public class ReflectionUtil implements SmartParamConsts {

    public static Reflections getReflectionsForPackages(PackageList packageList) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        for (String packageStem : packageList) {
            builder.addUrls(ClasspathHelper.forPackage(packageStem));
        }

        return builder.build();
    }

    public static Reflections getReflectionsForDefaultPackage() {
        return new Reflections(BASE_PACKAGE_PREFIX);
    }

    public static Map<String, Class<?>> lookupAnnotatedClasses(Reflections reflections, AnnotationHandler handler) {
        Map<String, Class<?>> types = new HashMap<String, Class<?>>();

        Annotation typeAnnotation;
        for (Class<?> type : reflections.getTypesAnnotatedWith(handler.handledAnnotation())) {
            typeAnnotation = type.getAnnotation(handler.handledAnnotation());
            types.put(handler.extractIdentifier(typeAnnotation), type);
        }

        return types;
    }
}
