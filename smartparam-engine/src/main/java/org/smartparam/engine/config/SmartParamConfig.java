package org.smartparam.engine.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.cache.MapParamCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.engine.ParamPreparer;
import org.smartparam.engine.core.engine.SmartParamPreparer;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.SmartInvokerRepository;
import org.smartparam.engine.core.repository.SmartMatcherRepository;
import org.smartparam.engine.core.repository.SmartTypeRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionManager;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.SmartFunctionManager;
import org.smartparam.engine.core.service.SmartFunctionProvider;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamConfig {

    private List<String> packagesToScan = new ArrayList<String>();

    private boolean scanPackages = true;

    @ConfigElement(SmartParamPreparer.class)
    private ParamPreparer paramPreparer;

    @ConfigElement(MapParamCache.class)
    private ParamCache paramCache;

    @ConfigElement(SmartFunctionManager.class)
    private FunctionManager functionManager;

    @ConfigElement(SmartFunctionProvider.class)
    private FunctionProvider functionProvider;

    @ConfigElement(MapFunctionCache.class)
    private FunctionCache functionCache;

    @ConfigElement(SmartInvokerRepository.class)
    private InvokerRepository invokerRepository;

    private ParamRepository paramRepository;

    @ConfigElement(value = Map.class, registerAt = FunctionProvider.class)
    private Map<String, FunctionRepository> functionRepositories;

    @ConfigElement(SmartTypeRepository.class)
    private TypeRepository typeRepository;

    @ConfigElement(SmartMatcherRepository.class)
    private MatcherRepository matcherRepository;

    @ConfigElement(value = Map.class, registerAt = InvokerRepository.class)
    private Map<String, FunctionInvoker> functionInvokers;

    @ConfigElement(value = Map.class, registerAt = TypeRepository.class)
    private Map<String, Type<?>> types;

    @ConfigElement(value = Map.class, registerAt = MatcherRepository.class)
    private Map<String, Matcher> matchers;

    public List<String> getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public boolean isScanAnnotations() {
        return scanPackages;
    }

    public void setScanAnnotations(boolean scanPackages) {
        this.scanPackages = scanPackages;
    }

    public ParamPreparer getParamPreparer() {
        return paramPreparer;
    }

    public void setParamPreparer(ParamPreparer paramPreparer) {
        this.paramPreparer = paramPreparer;
    }

    public ParamCache getParamCache() {
        return paramCache;
    }

    public void setParamCache(ParamCache paramCache) {
        this.paramCache = paramCache;
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }

    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public void setFunctionCache(FunctionCache functionCache) {
        this.functionCache = functionCache;
    }

    public InvokerRepository getInvokerRepository() {
        return invokerRepository;
    }

    public void setInvokerRepository(InvokerRepository invokerRepository) {
        this.invokerRepository = invokerRepository;
    }

    public ParamRepository getParamRepository() {
        return paramRepository;
    }

    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    public Map<String, FunctionRepository> getFunctionRepositories() {
        return functionRepositories;
    }

    public void setFunctionRepositories(Map<String, FunctionRepository> functionRepositories) {
        this.functionRepositories = functionRepositories;
    }

    public TypeRepository getTypeRepository() {
        return typeRepository;
    }

    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public MatcherRepository getMatcherRepository() {
        return matcherRepository;
    }

    public void setMatcherRepository(MatcherRepository matcherRepository) {
        this.matcherRepository = matcherRepository;
    }

    public Map<String, FunctionInvoker> getFunctionInvokers() {
        return functionInvokers;
    }

    public void setFunctionInvokers(Map<String, FunctionInvoker> functionInvokers) {
        this.functionInvokers = functionInvokers;
    }

    public Map<String, Type<?>> getTypes() {
        return types;
    }

    public void setTypes(Map<String, Type<?>> types) {
        this.types = types;
    }

    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = matchers;
    }
}
