package org.smartparam.engine.core.service;

import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionProvider {

    void registerRepository(String type, int order, FunctionRepository repository);

    void registerRepository(String[] types, int order, FunctionRepository repository);

    Iterable<RepositoryObjectKey> registeredRepositories();

    Function getFunction(String functionName);

    FunctionCache getFunctionCache();
}
