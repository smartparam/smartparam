package org.smartparam.demo.param;

import org.smartparam.demo.model.DemoModelObject;
import org.smartparam.engine.core.context.DefaultContext;

/**
 * SmartParam context for Demo application.
 * The context will be passed to all level creator functions, which try to oust
 * values to match level patterns.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class DemoParamContext extends DefaultContext {

    private DemoModelObject demoModelObject;

    public DemoModelObject getDemoModelObject() {
        return demoModelObject;
    }

    public void setDemoModelObject(DemoModelObject demoModelObject) {
        this.demoModelObject = demoModelObject;
    }
}
