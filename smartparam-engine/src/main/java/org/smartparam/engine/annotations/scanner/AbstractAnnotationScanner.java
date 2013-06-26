package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 * Helper class providing methods for common annotation scanner tasks.
 * TODO #ad any ideas on how to rename it?
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
abstract class AbstractAnnotationScanner {

    /**
     * Default name of "value" method, that holds entity name in SmartParam*
     * annotations.
     */
    private static final String VALUE_METHOD_NAME = "value";

    /**
     * Return a {@link Reflections} objects for packages list, with given
     * scanners configured (for example for method scanning).
     *
     * @param packageList    object encapsulating packages that should be scanned
     * @param customScanners scanners to configure {@link Reflections}
     *
     * @return reflections object, ready to scan
     */
    protected Reflections getReflectionsForPackages(PackageList packageList, Scanner... customScanners) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        FilterBuilder filterBuilder = new FilterBuilder();
        for (String packageName : packageList) {
            filterBuilder.includePackage(packageName);
            builder.addUrls(ClasspathHelper.forPackage(packageName));
        }
        builder.filterInputsBy(filterBuilder);
        builder.addScanners(customScanners);

        return builder.build();
    }

    /**
     * Extract value from {@link AbstractAnnotationScanner#VALUE_METHOD_NAME}
     * annotation method, uses {@link #extractValue(java.lang.annotation.Annotation, java.lang.String) }
     * internally.
     *
     * @param annotation source annotation
     *
     * @return value of value()
     */
    protected String extractValue(Annotation annotation) {
        return (String) extractValue(annotation, VALUE_METHOD_NAME);
    }

    /**
     * Extract value from given annotation method, if anything goes wrong
     * throws {@link SmartParamInitializationException} with real reason as
     * cause.
     *
     * @param annotation source annotation
     * @param methodName annotation method to look for
     *
     * @return value returned from annotation method
     */
    protected Object extractValue(Annotation annotation, String methodName) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(methodName);
            return defaultValueMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + methodName + " field found on annotation " + ClassUtils.getShortCanonicalName(annotation.annotationType()));
        }
    }
}
