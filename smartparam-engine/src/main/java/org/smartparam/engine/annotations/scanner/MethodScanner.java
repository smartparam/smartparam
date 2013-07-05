package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface MethodScanner {

    Map<String, Method> scanMethods(Class<? extends Annotation> annotationClass);

}
