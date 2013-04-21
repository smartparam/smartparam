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
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotatedMethodsScanner extends AbstractAnnotationScanner {

    public Map<String, Method> getAnnotatedMethods(PackageList packagesToScan, Class<? extends Annotation> annotationClass) {
        Map<String, Method> methods = new HashMap<String, Method>();
        
        Reflections reflections = getReflectionsForPackages(packagesToScan, new MethodAnnotationsScanner());

        String pluginName;
        for(Method method : reflections.getMethodsAnnotatedWith(annotationClass)) {
            pluginName = extractValue(method.getAnnotation(annotationClass));
            checkForDuplicates(methods, pluginName, method);
            methods.put(pluginName, method);
        }

        return methods;
    }

    private void checkForDuplicates(Map<String, Method> methods, String newPluginName, Method newPluginMethod) {
        if(methods.containsKey(newPluginName)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_TYPE_CODE,
                    "plugin " + newPluginName + " found at method " + newPluginMethod.toGenericString() + " was already registered using "
                    + methods.get(newPluginName).toGenericString() + " method");
        }
    }
}
