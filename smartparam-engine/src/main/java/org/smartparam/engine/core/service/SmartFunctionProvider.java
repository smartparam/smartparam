package org.smartparam.engine.core.service;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.AbstractAnnotationScanningRepository;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.util.RepositoryHelper;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartFunctionProvider extends AbstractAnnotationScanningRepository<FunctionRepository> implements FunctionProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<RepositoryObjectKey, FunctionRepository> repositories = new TreeMap<RepositoryObjectKey, FunctionRepository>();

    private FunctionCache functionCache;

    @Override
    public void register(String type, int order, FunctionRepository repository) {
        RepositoryObjectKey objectKey = new RepositoryObjectKey(type, order);

        if (repositories.containsKey(objectKey)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE, "other function repository has been already registered for " + type + " type");
        }
        logger.info("registering function repository at index: {} -> {}", type, repository.getClass());
        repositories.put(objectKey, repository);
    }

    @Override
    public Map<RepositoryObjectKey, FunctionRepository> registeredItems() {
        return repositories;
    }

    @Override
    public void setItems(Map<String, FunctionRepository> items) {
        RepositoryHelper.registerItems(this, items);
    }

    @Override
    public Function getFunction(String functionName) {
        Function function = functionCache.get(functionName);

        if (function == null) {
            function = searchForFunction(functionName);
            if (function == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_FUNCTION, "Unknown function: " + functionName);
            }
            functionCache.put(functionName, function);
        }

        return function;
    }

    private Function searchForFunction(String functionName) {
        Function function = null;
        for (FunctionRepository repository : repositories.values()) {
            function = repository.loadFunction(functionName);
            if (function != null) {
                break;
            }
        }

        return function;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionRepository.class;
    }

    @Override
    protected void handleRegistration(RepositoryObjectKey key, FunctionRepository repository) {
        repositories.put(key, repository);
    }

    @Override
    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    @Override
    public void setFunctionCache(FunctionCache cache) {
        this.functionCache = cache;
    }
}
