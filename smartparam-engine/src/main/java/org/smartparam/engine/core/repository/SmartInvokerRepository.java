package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartInvokerRepository extends AbstractRepository<FunctionInvoker<?>> implements InvokerRepository {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private Map<String, FunctionInvoker<?>> invokers = new HashMap<String, FunctionInvoker<?>>();

    public static SmartInvokerRepository createAndInitialize(AnnotationScannerProperties scannerProperties) {
        SmartInvokerRepository invokerRepository = new SmartInvokerRepository();
        invokerRepository.setScannerProperties(scannerProperties);

        invokerRepository.scan();
        return invokerRepository;
    }

    public <T extends Function> void registerInvoker(String implCode, FunctionInvoker<T> invoker) {
        logger.info("registering function invoker: {} -> {}", implCode, invoker.getClass());
        invokers.put(implCode, invoker);
    }

    @SuppressWarnings("unchecked")
    public <T extends Function> FunctionInvoker<T> getInvoker(T function) {
        return (FunctionInvoker<T>) invokers.get(function.getType());
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionInvoker.class;
    }

    @Override
    protected void handleRegistration(String objectCode, FunctionInvoker<?> objectToRegister) {
        registerInvoker(objectCode, objectToRegister);
    }

    public void setInvokers(Map<String, FunctionInvoker<?>> map) {
        for (Map.Entry<String, FunctionInvoker<?>> e : map.entrySet()) {
            String implCode = e.getKey();
            FunctionInvoker<? extends Function> invoker = e.getValue();
            registerInvoker(implCode, invoker);
        }
    }
}
