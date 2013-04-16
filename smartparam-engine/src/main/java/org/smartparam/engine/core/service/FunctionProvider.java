package org.smartparam.engine.core.service;

import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionProvider {

    void registerRepository(String type, FunctionRepository repository);

    void registerRepository(String[] types, FunctionRepository repository);

    Iterable<String> registeredRepositories();

    Function getFunction(String functionName);

    FunctionCache getFunctionCache();
}
