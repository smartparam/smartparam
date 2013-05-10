package org.smartparam.engine.core.repository;

import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionRepository {

    Function loadFunction(String functionName);
}
