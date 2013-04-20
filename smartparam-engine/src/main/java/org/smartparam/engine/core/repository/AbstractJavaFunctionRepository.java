package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.scanner.AnnotatedMethodsScanner;
import org.smartparam.engine.core.AbstractAnnotationScanner;
import org.smartparam.engine.model.function.Function;

/**
 * Klasa bazowa dla FunctionInvokerow, ktore wykonuja funkcje bazujace na
 * implementacji poprzez metode javy. Klasa zapewnia:
 * <ul>
 * <li>odnajdowanie metod pasujacych do wywolania (sprawdzanie kompatybilnosci
 * parametrow),
 * <li>cache dla refleksji przyspieszajacy znajdowanie metody okolo 5 razy
 * </ul>
 *
 * @param <FUNCTION>
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class AbstractJavaFunctionRepository extends AbstractAnnotationScanner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Map<String, Function> loadFunctions() {
        Map<String, Function> loadedFunctions = new HashMap<String, Function>();

        AnnotatedMethodsScanner methodsScanner = new AnnotatedMethodsScanner();
        Map<String, Method> scannedMethods = methodsScanner.getAnnotatedMethods(getScannerProperties().getPackagesToScan(), annotationClass());

        String functionName;
        for (Map.Entry<String, Method> methodEntry : scannedMethods.entrySet()) {
            functionName = methodEntry.getKey();
            logger.info("registering function: {} -> {}", functionName, methodEntry.getValue().toGenericString());
            loadedFunctions.put(functionName, createFunction(functionName, methodEntry.getValue()));
        }

        return loadedFunctions;
    }

    protected abstract Class<? extends Annotation> annotationClass();

    protected abstract Function createFunction(String functionName, Method method);
}
