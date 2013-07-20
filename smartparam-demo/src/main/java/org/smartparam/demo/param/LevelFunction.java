package org.smartparam.demo.param;

import org.smartparam.engine.annotations.JavaPlugin;

/**
 * SmartParam level creator functions.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class LevelFunction {

    @JavaPlugin("level.sample")
    public String demoModelObject(DemoParamContext context) {
        if(context.getDemoModelObject() != null) {
            return context.getDemoModelObject().getCode();
        }

        return null;
    }
}
