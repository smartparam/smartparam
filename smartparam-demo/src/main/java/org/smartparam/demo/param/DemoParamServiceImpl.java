package org.smartparam.demo.param;

import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.type.AbstractHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of demo parameter service.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Service("demoParamService")
public class DemoParamServiceImpl implements DemoParamService {

    @Autowired
    private ParamEngine paramEngine;

    @Override
    public AbstractHolder get(String paramName, DemoParamContext context) {
        return paramEngine.getValue(paramName, context);
    }
}
