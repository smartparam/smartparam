package org.smartparam.engine.core.engine;

import java.util.List;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmarParamConfig {

    private List<Type> registeredTypes;

    private List<FunctionInvoker> registeredInvokers;

    private List<Matcher> registeredMatchers;

    private FunctionCache functionCache;

    private ParamCache paramCache;

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public ParamCache getParamCache() {
        return paramCache;
    }
}
