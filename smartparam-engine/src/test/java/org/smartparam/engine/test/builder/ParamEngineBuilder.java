package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.SmartParamEngine;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParamEngineBuilder {

    private ParamEngine paramEngine;

    private ParamEngineBuilder() {
        this.paramEngine = new SmartParamEngine();
    }
    
    

}
