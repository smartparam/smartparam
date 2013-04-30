package org.smartparam.engine.core.service;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.AnnotationScanner;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.MapFunctionCache;
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

    private FunctionCache functionCache = null;

    public static SmartFunctionProvider createAndInitialize(AnnotationScannerProperties scannerProperties) {
        SmartFunctionProvider functionProvider = new SmartFunctionProvider();
        functionProvider.setScannerProperties(scannerProperties);

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

    public void register(String type, int order, FunctionRepository repository) {
        if (repositories.containsKey(new RepositoryObjectKey(type))) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_TYPE_CODE, "other function repository has been already registered for " + type + " type");
        }
        logger.info("registering function repository at index: {} -> {}", type, repository.getClass());
        repositories.put(new RepositoryObjectKey(type, order), repository);
    }

    public Map<RepositoryObjectKey, FunctionRepository> registeredItems() {
        return repositories;
    }

    public void setItems(Map<String, FunctionRepository> items) {
        RepositoryHelper.registerItems(this, items);
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
    protected void handleRegistration(RepositoryObjectKey key, FunctionRepository repository) {
        repositories.put(key, repository);

        // TODO #ad maybe this can be done better?
        if (repository instanceof AnnotationScanner) {
            ((AnnotationScanner) repository).setScannerProperties(getScannerProperties());
        }
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public void setFunctionCache(FunctionCache cache) {
        this.functionCache = cache;
    }
}
