package org.smartparam.demo.param;

import org.smartparam.engine.annotations.SmartParamJavaPlugin;

/**
 * SmartParam level creator functions.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class LevelFunction {

    @SmartParamJavaPlugin("level.sample")
    public String demoModelObject(DemoParamContext context) {
        if(context.getDemoModelObject() != null) {
            return context.getDemoModelObject().getCode();
        }

        return null;
    }
}
