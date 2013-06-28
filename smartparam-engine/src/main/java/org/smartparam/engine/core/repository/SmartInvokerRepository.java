package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartInvokerRepository extends AbstractAnnotationScanningRepository<FunctionInvoker> implements InvokerRepository {

    private MapRepository<FunctionInvoker> innerRepository = new MapRepository<FunctionInvoker>(FunctionInvoker.class);

    @Override
    public void register(String code, FunctionInvoker invoker) {
        innerRepository.register(code, invoker);
    }

    @Override
    public FunctionInvoker getInvoker(Function function) {
        return innerRepository.getItem(function.getType());
    }

    @Override
    public Map<String, FunctionInvoker> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void setItems(Map<String, FunctionInvoker> invokers) {
        innerRepository.setItemsUnordered(invokers);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionInvoker.class;
    }

    @Override
    protected void handleRegistration(RepositoryObjectKey key, FunctionInvoker functionInvoker) {
        register(key.getKey(), functionInvoker);
    }
}
