package org.smartparam.engine.core.engine;

import javax.annotation.PostConstruct;
import org.smartparam.engine.core.loader.FunctionLoader;
import org.smartparam.engine.core.loader.ParamProvider;
import org.smartparam.engine.core.provider.SmartFunctionProvider;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BaseSmartParamEngine extends SmartParamEngine {

    private ParamProvider paramProvider = null;

    private FunctionLoader functionLoader = null;

    @PostConstruct
    @Override
    public void initializeProviders() {
        super.initializeProviders();

        if (!hasFunctionProvider()) {
            SmartFunctionProvider smartFunctionProvider = new SmartFunctionProvider();
            smartFunctionProvider.setLoader(functionLoader);
            smartFunctionProvider.initializeProviders();
            setFunctionProvider(smartFunctionProvider);
        }

        if (!hasParamPreparer()) {
            SmartParamPreparer smartParamPreparer = new SmartParamPreparer(isScanAnnotations(), getPackagesToScan());
            smartParamPreparer.setParamProvider(paramProvider);
            smartParamPreparer.initializeProviders();
            setParamPreparer(smartParamPreparer);
        }
    }

    public void setParamProvider(ParamProvider paramProvider) {
        this.paramProvider = paramProvider;
    }

    public void setFunctionLoader(FunctionLoader functionLoader) {
        this.functionLoader = functionLoader;
    }
}
