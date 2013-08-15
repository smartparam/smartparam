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
 *
 * @author Adam Dubiel
 */
public class PackageMethodScanner implements MethodScanner {

    private ReflectionsScanner reflectionsScanner = new ReflectionsScanner();

    private PackageList packagesToScan;

    public PackageMethodScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

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

    private void checkForDuplicates(Map<String, Method> methods, String newPluginName, Method newPluginMethod) {
        if (methods.containsKey(newPluginName)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE,
                    String.format("can't register tow methods with same name, plugin %s found at method %s was already registered with %s method",
                    newPluginName, newPluginMethod.toGenericString(), methods.get(newPluginName).toGenericString()));
        }
    }

    public void setReflectionsScanner(ReflectionsScanner reflectionsScanner) {
        this.reflectionsScanner = reflectionsScanner;
    }
}
