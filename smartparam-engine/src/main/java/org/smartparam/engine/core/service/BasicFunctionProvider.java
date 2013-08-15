package org.smartparam.engine.core.service;

import java.util.Map;
import java.util.TreeMap;
import org.smartparam.engine.annotations.ParamFunctionRepository;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.annotations.scanner.TypeScanner;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.TypeScanningRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 */
public class BasicFunctionProvider implements FunctionProvider, TypeScanningRepository {

    private MapRepository<FunctionRepository> innerRepository = new MapRepository<FunctionRepository>(FunctionRepository.class, new TreeMap<RepositoryObjectKey, FunctionRepository>());

    private FunctionCache functionCache;

    @Override
    public void scanAnnotations(TypeScanner scanner) {
        Map<RepositoryObjectKey, FunctionRepository> repositories = scanner.scanTypes(ParamFunctionRepository.class);
        innerRepository.registerAll(repositories);
    }

    @Override
    public void register(String type, int order, FunctionRepository repository) {
        RepositoryObjectKey objectKey = new RepositoryObjectKey(type, order);
        innerRepository.registerUnique(objectKey, repository);
    }

    @Override
    public Map<String, FunctionRepository> registeredItems() {
        return innerRepository.getItemsOrdered();
    }

    @Override
    public void registerAll(Map<String, FunctionRepository> items) {
        innerRepository.registerAllOrdered(items);
    }

    @Override
    public Function getFunction(String functionName) {
        Function function = functionCache.get(functionName);

        if (function == null) {
            function = searchForFunction(functionName);
            if (function == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_FUNCTION,
                        String.format("Could not find function %s in any registered repository. "
                        + "Check if all repositories are properly configured."
                        + "To see all functions registered, follow logs from MapRepository on INFO level.", functionName));
            }
            functionCache.put(functionName, function);
        }

        return function;
    }

    private Function searchForFunction(String functionName) {
        Function function = null;
        for (FunctionRepository repository : innerRepository.getItemsOrdered().values()) {
            function = repository.loadFunction(functionName);
            if (function != null) {
                break;
            }
        }

        return function;
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
