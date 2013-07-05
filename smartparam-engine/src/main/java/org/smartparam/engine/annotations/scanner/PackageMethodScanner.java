package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.util.reflection.AnnotationHelper;
import org.smartparam.engine.util.reflection.ReflectionsScanner;

/**
 * Annotation scanner util specializing in scanning methods.
 *
 * @see #getAnnotatedMethods(java.lang.Class)
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class PackageMethodScanner implements MethodScanner {

    private ReflectionsScanner reflectionsScanner = new ReflectionsScanner();

    private PackageList packagesToScan;

    public PackageMethodScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

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
    @Override
    public Map<String, Method> scanMethods(Class<? extends Annotation> annotationClass) {
        Map<String, Method> methods = new HashMap<String, Method>();

        String pluginName;
        for (Method method : reflectionsScanner.findMethodsAnnotatedWith(annotationClass, packagesToScan.getPackages())) {
            pluginName = AnnotationHelper.extractValue(method.getAnnotation(annotationClass), "value");
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
     * @param methods         map of registered methods
     * @param newPluginName   new method name, should be unique in methods map
     * @param newPluginMethod method, for exception reporting
     */
    private void checkForDuplicates(Map<String, Method> methods, String newPluginName, Method newPluginMethod) {
        if (methods.containsKey(newPluginName)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE,
                    "plugin " + newPluginName + " found at method " + newPluginMethod.toGenericString() + " was already registered using "
                    + methods.get(newPluginName).toGenericString() + " method");
        }
    }

    public void setReflectionsScanner(ReflectionsScanner reflectionsScanner) {
        this.reflectionsScanner = reflectionsScanner;
    }
}
