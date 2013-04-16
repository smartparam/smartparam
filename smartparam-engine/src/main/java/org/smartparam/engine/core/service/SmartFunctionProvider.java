package org.smartparam.engine.core.service;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.repository.AbstractRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartFunctionProvider extends AbstractRepository<FunctionRepository> implements FunctionProvider {

    private Map<String, FunctionRepository> repositories = new HashMap<String, FunctionRepository>();

    private FunctionCache functionCache = null;

    public static SmartFunctionProvider createAndInitialize() {
        SmartFunctionProvider functionProvider = new SmartFunctionProvider();
        functionProvider.initialize();
        return functionProvider;
    }

    @PostConstruct
    public void initialize() {
        super.scan();
        if (functionCache == null) {
            functionCache = new MapFunctionCache();
        }
        populateCache();
    }

    public void registerRepository(String type, FunctionRepository repository) {
        repositories.put(type, repository);
    }

    public void registerRepository(String[] types, FunctionRepository repository) {
        for (String type : types) {
            repositories.put(type, repository);
        }
    }

    public Iterable<String> registeredRepositories() {
        return repositories.keySet();
    }

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
            if (repository.repositoryCapabilities().isSupportsSingle()) {
                function = repository.loadFunction(functionName);
                if (function != null) {
                    break;
                }
            }
        }

        return function;
    }

    private void populateCache() {
        for (FunctionRepository repository : repositories.values()) {
            if (repository.repositoryCapabilities().isSupportsBatch()) {
                functionCache.putAll(repository.loadFunctions());
            }
        }
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamFunctionRepository.class;
    }

    @Override
    protected void handleRegistration(String functionTypeCode, FunctionRepository repository) {
        repositories.put(functionTypeCode, repository);
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public void setFunctionCache(FunctionCache cache) {
        this.functionCache = cache;
    }
}
