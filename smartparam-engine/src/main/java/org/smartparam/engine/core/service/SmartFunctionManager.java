package org.smartparam.engine.core.service;

import javax.annotation.PostConstruct;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.engine.AbstractAnnotationScanner;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.loader.FunctionLoader;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.core.repository.SmartInvokerRepository;
import org.smartparam.engine.model.function.Function;

/**
 * Service Provider, ktory dostarcza funkcje z repozytorium o zadanej nazwie.
 * Pobiera funkcje przy pomocy loadera ({@link FunctionLoader}), ktorego
 * zadaniem jest fizyczne wczytani funkcji z bazy danych. Wczytana funkcja jest
 * cache'owana przy pomocy {@link FunctionCache}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartFunctionManager extends AbstractAnnotationScanner implements FunctionManager {

    private InvokerRepository invokerRepository = null;

    private FunctionProvider functionProvider = null;

    public static SmartFunctionManager createAndInitialize(AnnotationScannerProperties scannerProperties) {
        SmartFunctionManager functionManager = new SmartFunctionManager();
        functionManager.setScannerProperties(scannerProperties);

        functionManager.initialize();
        return functionManager;
    }

    @PostConstruct
    public void initialize() {
        if (invokerRepository == null) {
            invokerRepository = SmartInvokerRepository.createAndInitialize(getScannerProperties());
        }
        if (functionProvider == null) {
            functionProvider = SmartFunctionProvider.createAndInitialize(getScannerProperties());
        }
    }

    public Object invokeFunction(String name, Object... args) {
        Function function = functionProvider.getFunction(name);
        return invokeFunction(function, args);
    }

    public Object invokeFunction(Function function, Object... args) {
        FunctionInvoker invoker = invokerRepository.getInvoker(function);

        if (invoker == null) {
            throw new SmartParamException(SmartParamErrorCode.UNDEFINED_FUNCTION_INVOKER, "Undefined FunctionInvoker for: " + function);
        }

        try {
            return invoker.invoke(function, args);
        } catch (RuntimeException e) {
            throw new SmartParamException(SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e, "Failed to invoke function: " + function);
        }
    }

    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

    public InvokerRepository getInvokerRepository() {
        return invokerRepository;
    }

    public void setInvokerRepository(InvokerRepository invokerRepository) {
        this.invokerRepository = invokerRepository;
    }
}
