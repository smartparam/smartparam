package org.smartparam.engine.core.config;

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

    private PackageList packagesToScan = new PackageList();

    private boolean scanPackages = true;

    @ConfigDefault(SmartParamPreparer.class)
    private ParamPreparer paramPreparer;

    @ConfigDefault(MapParamCache.class)
    private ParamCache paramCache;

    @ConfigDefault(SmartFunctionManager.class)
    private FunctionManager functionManager;

    @ConfigDefault(SmartFunctionProvider.class)
    private FunctionProvider functionProvider;

    @ConfigDefault(MapFunctionCache.class)
    private FunctionCache functionCache;

    @ConfigDefault(SmartInvokerRepository.class)
    private InvokerRepository invokerRepository;

    private ParamRepository paramRepository;

    @ConfigDefault(emptyMap = true)
    private Map<String, FunctionRepository> functionRepositories;

    @ConfigDefault(SmartTypeRepository.class)
    private TypeRepository typeRepository;

    @ConfigDefault(SmartMatcherRepository.class)
    private MatcherRepository matcherRepository;

    @ConfigDefault(emptyMap = true)
    private Map<String, FunctionInvoker> functionInvokers;

    @ConfigDefault(emptyMap = true)
    private Map<String, Type<?>> types;

    @ConfigDefault(emptyMap = true)
    private Map<String, Matcher> matchers;

    public PackageList getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public boolean isScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(boolean scanPackages) {
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
