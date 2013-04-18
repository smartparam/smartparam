package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public abstract class AbstractAnnotationScanner {

    private final static String VALUE_METHOD_NAME = "value";

    protected Reflections getReflectionsForPackages(PackageList packageList, Scanner... customScanners) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        for (String packageStem : packageList) {
            builder.addUrls(ClasspathHelper.forPackage(packageStem));
        }
        builder.addScanners(customScanners);

        return builder.build();
    }

    protected String extractValue(Annotation annotation) {
        return (String) extractValue(annotation, VALUE_METHOD_NAME);
    }

    protected Object extractValue(Annotation annotation, String methodName) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(methodName);
            return defaultValueMethod.invoke(annotation);
        } catch (Throwable exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + methodName + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }

}
