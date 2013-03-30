package org.smartparam.engine.core.engine;

import javax.annotation.PostConstruct;

/**
 * ParamEngine decorator
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedParamEngine extends ParamEngine {


    @PostConstruct
    public void loadFromAnnotations() {
        if(!hasParamProvider()) {
            
        }
    }
}
