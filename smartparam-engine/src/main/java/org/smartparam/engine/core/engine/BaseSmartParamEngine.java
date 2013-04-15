package org.smartparam.engine.core.engine;

import javax.annotation.PostConstruct;
import org.smartparam.engine.core.loader.ParamProvider;
import org.smartparam.engine.core.service.SmartFunctionManager;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BaseSmartParamEngine extends SmartParamEngine {

    private ParamProvider paramProvider = null;

    @PostConstruct
    //@Override
    public void initializeProviders() {
        //super.initializeProviders();

        if (!hasFunctionManager()) {
            SmartFunctionManager functionManager = SmartFunctionManager.createAndInitialize();
            setFunctionManager(functionManager);
        }

        if (!hasParamPreparer()) {
            SmartParamPreparer smartParamPreparer = new SmartParamPreparer(isScanAnnotations(), getPackagesToScan());
            smartParamPreparer.setLoader(paramProvider);
            smartParamPreparer.initializeProviders();
            setParamPreparer(smartParamPreparer);
        }
    }

    public void setParamProvider(ParamProvider paramProvider) {
        this.paramProvider = paramProvider;
    }
}
