package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.engine.SmartParamPreparer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParamEngineBuilder {

    private SmartParamEngine paramEngine;

    private SmartParamPreparer paramPreparer;

    private ParamEngineBuilder() {
        this.paramEngine = new SmartParamEngine();
        this.paramPreparer = new SmartParamPreparer();

        this.paramEngine.setParamPreparer(paramPreparer);
    }



}
