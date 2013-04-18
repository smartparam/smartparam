package org.smartparam.engine.core.engine;

import javax.annotation.PostConstruct;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.service.SmartFunctionManager;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BaseSmartParamEngine extends SmartParamEngine {

    private ParamRepository paramProvider = null;

    @PostConstruct
    public void initializeProviders() {
        //super.initializeProviders();

        if (!hasFunctionManager()) {
            SmartFunctionManager functionManager = SmartFunctionManager.createAndInitialize(getScannerProperties());
            setFunctionManager(functionManager);
        }

        if (!hasParamPreparer()) {
            SmartParamPreparer smartParamPreparer = new SmartParamPreparer();
            smartParamPreparer.setParamRepository(paramProvider);
            smartParamPreparer.setFunctionProvider(getFunctionManager().getFunctionProvider());
            smartParamPreparer.setScannerProperties(getScannerProperties());

            smartParamPreparer.initializeProviders();
            setParamPreparer(smartParamPreparer);
        }
    }

    public void setParamProvider(ParamRepository paramProvider) {
        this.paramProvider = paramProvider;
    }
}
