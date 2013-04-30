package org.smartparam.engine.core.service;

import org.smartparam.engine.core.OrderedRepository;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionProvider extends OrderedRepository<FunctionRepository> {

    Function getFunction(String functionName);

    FunctionCache getFunctionCache();

    void setFunctionCache(FunctionCache functionCache);
}
