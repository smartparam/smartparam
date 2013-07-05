package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import org.smartparam.engine.annotations.scanner.MethodScanner;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.model.function.Function;

public abstract class AbstractJavaFunctionRepository implements FunctionRepository, MethodScanningRepository {

    private MapRepository<Function> innerRepository = new MapRepository<Function>(functionClass());

    @Override
    public void scanMethods(MethodScanner scanner) {
        Map<String, Method> scannedMethods = scanner.scanMethods(annotationClass());

        String functionName;
        for (Map.Entry<String, Method> methodEntry : scannedMethods.entrySet()) {
            functionName = methodEntry.getKey();
            innerRepository.register(functionName, createFunction(functionName, methodEntry.getValue()));
        }
    }

    @Override
    public Function loadFunction(String functionName) {
        return innerRepository.getItem(functionName);
    }

    protected abstract Class<? extends Function> functionClass();

    protected abstract Class<? extends Annotation> annotationClass();

    protected abstract Function createFunction(String functionName, Method method);
}
