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
package org.smartparam.engine.annotated.repository;

import java.util.Map;
import java.util.TreeMap;
import org.smartparam.engine.annotated.annotations.ParamFunctionRepository;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.function.FunctionProvider;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.function.UnknownFunctionException;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningFunctionProvider implements FunctionProvider, TypeScanningRepository {

    private final MapRepository<FunctionRepository> innerRepository = new MapRepository<FunctionRepository>(FunctionRepository.class, new TreeMap<RepositoryObjectKey, FunctionRepository>());

    private final FunctionCache functionCache;

    public ScanningFunctionProvider(FunctionCache functionCache) {
        this.functionCache = functionCache;
    }

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, FunctionRepository> repositories = scanner.scanTypes(ParamFunctionRepository.class);
        componentInitializerRunner.runInitializersOnList(repositories.values());
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
    public void registerWithKeys(Map<RepositoryObjectKey, FunctionRepository> objects) {
        innerRepository.registerAll(objects);
    }

    @Override
    public Function getFunction(String functionName) {
        Function function = functionCache.get(functionName);

        if (function == null) {
            function = searchForFunction(functionName);
            if (function == null) {
                throw new UnknownFunctionException(functionName);
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
}
