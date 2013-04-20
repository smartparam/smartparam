package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartInvokerRepository extends AbstractAnnotationScanningRepository<FunctionInvoker> implements InvokerRepository {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private Map<String, FunctionInvoker> invokers = new HashMap<String, FunctionInvoker>();

    public static SmartInvokerRepository createAndInitialize(AnnotationScannerProperties scannerProperties) {
        SmartInvokerRepository invokerRepository = new SmartInvokerRepository();
        invokerRepository.setScannerProperties(scannerProperties);

        invokerRepository.scan();
        return invokerRepository;
    }

    public void registerInvoker(String implCode, FunctionInvoker invoker) {
        logger.info("registering function invoker: {} -> {}", implCode, invoker.getClass());
        invokers.put(implCode, invoker);
    }

    public FunctionInvoker getInvoker(Function function) {
        return invokers.get(function.getType());
    }

    public Map<String, FunctionInvoker> registeredInvokers() {
        return Collections.unmodifiableMap(invokers);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionInvoker.class;
    }

    @Override
    protected void handleRegistration(String objectCode, FunctionInvoker objectToRegister) {
        registerInvoker(objectCode, objectToRegister);
    }

    public void setInvokers(Map<String, FunctionInvoker> map) {
        for (Map.Entry<String, FunctionInvoker> e : map.entrySet()) {
            String implCode = e.getKey();
            FunctionInvoker invoker = e.getValue();
            registerInvoker(implCode, invoker);
        }
    }
}
