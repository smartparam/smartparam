/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.config.pico;

import java.util.Arrays;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.config.ComponentInitializer;
import org.smartparam.engine.config.ComponentInitializerRunner;
import org.smartparam.engine.config.initialization.MethodScannerInitializer;
import org.smartparam.engine.config.initialization.PostConstructInitializer;
import org.smartparam.engine.config.initialization.TypeScannerInitializer;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineConfigBuilder {

    private PicoParamEngineConfig paramEngineConfig;

    private ParamEngineConfigBuilder() {
        paramEngineConfig = new PicoParamEngineConfig();
    }

    public static ParamEngineConfigBuilder paramEngineConfig() {
        return new ParamEngineConfigBuilder();
    }

    public PicoParamEngineConfig build() {
        withComponentInitializers(new PostConstructInitializer());
        return paramEngineConfig;
    }

    public ParamEngineConfigBuilder withAnnotationScanEnabled(String... packagesToScan) {
        PackageList packageList = new PackageList();
        packageList.setPackages(Arrays.asList(packagesToScan));

        return withComponentInitializers(new TypeScannerInitializer(packageList), new MethodScannerInitializer(packageList));
    }

    public ParamEngineConfigBuilder withComponent(Object component) {
        paramEngineConfig.addComponent(component);
        return this;
    }

    public ParamEngineConfigBuilder withParameterRepositories(ParamRepository... repositories) {
        paramEngineConfig.getParameterRepositories().addAll(Arrays.asList(repositories));
        return this;
    }

    public ParamEngineConfigBuilder withFunctionRepository(String functionType, int priority, FunctionRepository repository) {
        paramEngineConfig.getFunctionRepositories().put(new RepositoryObjectKey(functionType, priority), repository);
        return this;
    }

    public ParamEngineConfigBuilder withFunctionInvoker(String functionType, FunctionInvoker invoker) {
        paramEngineConfig.getFunctionInvokers().put(functionType, invoker);
        return this;
    }

    public ParamEngineConfigBuilder withType(String code, Type<?> type) {
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
