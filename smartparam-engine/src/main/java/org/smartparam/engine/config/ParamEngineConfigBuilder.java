package org.smartparam.engine.config;

import java.util.Arrays;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.engine.ParamPreparer;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionManager;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.ParameterProvider;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineConfigBuilder {

    private ParamEngineConfig paramEngineConfig;

    private ParamEngineConfigBuilder() {
        paramEngineConfig = new ParamEngineConfig();
    }

    public static ParamEngineConfigBuilder paramEngineConfig() {
        return new ParamEngineConfigBuilder();
    }

    public ParamEngineConfig build() {
        withComponentInitializers(new PostConstructInitializer());
        return paramEngineConfig;
    }

    public ParamEngineConfigBuilder withAnnotationScanEnabled(String... packagesToScan) {
        PackageList packageList = new PackageList();
        packageList.setPackages(Arrays.asList(packagesToScan));

        return withComponentInitializers(new TypeScannerInitializer(packageList), new MethodScannerInitializer(packageList));
    }

    public ParamEngineConfigBuilder withParamPreparer(ParamPreparer paramPreparer) {
        paramEngineConfig.setParamPreparer(paramPreparer);
        return this;
    }

    public ParamEngineConfigBuilder withParamCache(ParamCache paramCache) {
        paramEngineConfig.setParamCache(paramCache);
        return this;
    }

    public ParamEngineConfigBuilder withFunctionManager(FunctionManager functionManager) {
        paramEngineConfig.setFunctionManager(functionManager);
        return this;
    }

    public ParamEngineConfigBuilder withFunctionProvider(FunctionProvider functionProvider) {
        paramEngineConfig.setFunctionProvider(functionProvider);
        return this;
    }

    public ParamEngineConfigBuilder withFunctionCache(FunctionCache functionCache) {
        paramEngineConfig.setFunctionCache(functionCache);
        return this;
    }

    public ParamEngineConfigBuilder withInvokerRepository(InvokerRepository invokerRepository) {
        paramEngineConfig.setInvokerRepository(invokerRepository);
        return this;
    }

    public ParamEngineConfigBuilder withParameterProvider(ParameterProvider parameterProvider) {
        paramEngineConfig.setParameterProvider(parameterProvider);
        return this;
    }

    public ParamEngineConfigBuilder withParameterRepositories(ParamRepository... repositories) {
        paramEngineConfig.getParameterRepositories().addAll(Arrays.asList(repositories));
        return this;
    }

    public ParamEngineConfigBuilder withFunctionRepository(String functionType, FunctionRepository repository) {
        paramEngineConfig.getFunctionRepositories().put(functionType, repository);
        return this;
    }

    public ParamEngineConfigBuilder withTypeRepository(TypeRepository typeRepository) {
        paramEngineConfig.setTypeRepository(typeRepository);
        return this;
    }

    public ParamEngineConfigBuilder withMatcherRepository(MatcherRepository matcherRepository) {
        paramEngineConfig.setMatcherRepository(matcherRepository);
        return this;
    }

    public ParamEngineConfigBuilder withFunctionInvoker(String functionType, FunctionInvoker invoker) {
        paramEngineConfig.getFunctionInvokers().put(functionType, invoker);
        return this;
    }

    public  ParamEngineConfigBuilder withType(String code, Type<?> type) {
        paramEngineConfig.getTypes().put(code, type);
        return this;
    }

    public ParamEngineConfigBuilder withMatcher(String code, Matcher matcher) {
        paramEngineConfig.getMatchers().put(code, matcher);
        return this;
    }

    public ParamEngineConfigBuilder withInitializationRunner(ComponentInitializerRunner runner) {
        paramEngineConfig.setInitializationRunner(runner);
        return this;
    }

    public ParamEngineConfigBuilder withComponentInitializers(ComponentInitializer... initializers) {
        paramEngineConfig.getComponentInitializers().addAll(Arrays.asList(initializers));
        return this;
    }
}
