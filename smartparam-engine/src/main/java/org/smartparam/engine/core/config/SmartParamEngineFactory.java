package org.smartparam.engine.core.config;

import java.util.Map;
import java.util.Map.Entry;
import org.smartparam.engine.core.OrderedRepository;
import org.smartparam.engine.core.Repository;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.service.FunctionManager;
import org.smartparam.engine.core.service.FunctionProvider;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamEngineFactory {

    private ConfigPreparer preparer = new ConfigPreparer();

    public ParamEngine getParamEngine(SmartParamConfig referenceConfig) {
        SmartParamConfig config = preparer.getPreparedConfig(referenceConfig);


    }

    private FunctionManager createFunctionManager(SmartParamConfig config) {
        FunctionManager functionManager = config.getFunctionManager();
        if (functionManager.getFunctionProvider() == null) {
            functionManager.setFunctionProvider(createFunctionProvider(config));
        }

        return functionManager;
    }

    private FunctionProvider createFunctionProvider(SmartParamConfig config) {
        FunctionProvider functionProvider = config.getFunctionProvider();

        if (functionProvider.getFunctionCache() == null) {
            functionProvider.setFunctionCache(config.getFunctionCache());
        }
        functionProvider.setItems(config.getFunctionRepositories());

        return functionProvider;
    }
}
