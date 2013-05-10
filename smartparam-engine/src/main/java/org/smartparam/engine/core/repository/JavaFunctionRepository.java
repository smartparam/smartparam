package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.annotations.SmartParamJavaPlugin;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.function.JavaFunction;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamFunctionRepository("java")
public class JavaFunctionRepository extends AbstractJavaFunctionRepository {

    @Override
    protected Class<? extends Annotation> annotationClass() {
        return SmartParamJavaPlugin.class;
    }

    @Override
    protected Function createFunction(String functionName, Method method) {
        return new JavaFunction(functionName, "java", method);
    }
}
