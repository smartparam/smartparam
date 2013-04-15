package org.smartparam.engine.core.service;

import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionProvider {

    Function getFunction(String functionName);

}
