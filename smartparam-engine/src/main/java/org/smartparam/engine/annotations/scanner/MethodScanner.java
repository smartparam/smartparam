package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Annotation scanner util specializing in scanning methods.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface MethodScanner {

    /**
     * Return all methods annotated with given annotation that can be found in
     * packages. Annotation should have a value() method returning string, as
     * its value will be used as method unique name. If more than one method
     * has same name, {@link SmartParamException} is thrown.
     *
     * @param annotationClass annotation to look for
     *
     * @return map (name -> method) of methods (no ordering guaranteed)
     */
    Map<String, Method> scanMethods(Class<? extends Annotation> annotationClass);
}
