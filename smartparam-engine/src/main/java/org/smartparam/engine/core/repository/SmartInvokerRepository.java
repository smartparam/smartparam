package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.util.RepositoryHelper;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartInvokerRepository extends AbstractAnnotationScanningRepository<FunctionInvoker> implements InvokerRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, FunctionInvoker> invokers = new HashMap<String, FunctionInvoker>();

    public void register(String code, FunctionInvoker invoker) {
        logger.info("registering function invoker: {} -> {}", code, invoker.getClass());
        invokers.put(code, invoker);
    }

    public FunctionInvoker getInvoker(Function function) {
        return invokers.get(function.getType());
    }

    public Map<String, FunctionInvoker> registeredItems() {
        return Collections.unmodifiableMap(invokers);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionInvoker.class;
    }

    @Override
    protected void handleRegistration(RepositoryObjectKey key, FunctionInvoker functionInvoker) {
        register(key.getKey(), functionInvoker);
    }

    public void setItems(Map<String, FunctionInvoker> map) {
        RepositoryHelper.registerItems(this, map);
    }
}
