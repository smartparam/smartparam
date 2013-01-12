package pl.generali.merkury.param.core.config;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.generali.merkury.param.core.engine.ParamEngine;
import pl.generali.merkury.param.core.function.FunctionInvoker;
import pl.generali.merkury.param.model.FunctionImpl;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class InvokerProvider {

    private final Logger logger = LoggerFactory.getLogger(ParamEngine.class);

    private Map<String, FunctionInvoker<?>> invokers = new HashMap<String, FunctionInvoker<?>>();

    public <T extends FunctionImpl> void registerInvoker(String implCode, FunctionInvoker<T> invoker) {
        logger.info("registering function invoker: {} -> {}", implCode, invoker.getClass());
        invokers.put(implCode, invoker);
    }

    @SuppressWarnings("unchecked")
    public <T extends FunctionImpl> FunctionInvoker<T> getInvoker(T function) {
        return (FunctionInvoker<T>) invokers.get(function.getImplCode());
    }

    public void setInvokers(Map<String, FunctionInvoker<?>> map) {
        for (Map.Entry<String, FunctionInvoker<?>> e : map.entrySet()) {
            String implCode = e.getKey();
            FunctionInvoker<? extends FunctionImpl> invoker = e.getValue();
            registerInvoker(implCode, invoker);
        }
    }
}
