package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 * Annotation scanner util specializing in scanning methods.
 *
 * @see #getAnnotatedMethods(org.smartparam.engine.bean.PackageList, java.lang.Class)
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class AnnotatedMethodsScanner extends AbstractAnnotationScanner {

    /**
     * Return all methods annotated with given annotation that can be found in
     * packages. Annotation should have a value() method returning string, as
     * its value will be used as method unique name. If more than one method
     * has same name, {@link SmartParamException} is thrown.
     *
     * @param packagesToScan  package list descriptor
     * @param annotationClass annotation to look for
     *
     * @return map (name -> method) of methods (no ordering guaranteed)
     */
    public Map<String, Method> getAnnotatedMethods(PackageList packagesToScan, Class<? extends Annotation> annotationClass) {
        Map<String, Method> methods = new HashMap<String, Method>();

        Reflections reflections = getReflectionsForPackages(packagesToScan, new MethodAnnotationsScanner());

        String pluginName;
        for (Method method : reflections.getMethodsAnnotatedWith(annotationClass)) {
            pluginName = extractValue(method.getAnnotation(annotationClass));
            checkForDuplicates(methods, pluginName, method);
            methods.put(pluginName, method);
        }

        return methods;
    }

    /**
     * Check if map already contains method registered under given name, if it
     * does {@link SmartParamException} with {@link SmartParamErrorCode#NON_UNIQUE_ITEM_CODE}
     * error code is thrown.
     *
     * @param methods map of registered methods
     * @param newPluginName new method name, should be unique in methods map
     * @param newPluginMethod method, for exception reporting
     */
    private void checkForDuplicates(Map<String, Method> methods, String newPluginName, Method newPluginMethod) {
        if (methods.containsKey(newPluginName)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE,
                    "plugin " + newPluginName + " found at method " + newPluginMethod.toGenericString() + " was already registered using "
                    + methods.get(newPluginName).toGenericString() + " method");
        }
    }
}
