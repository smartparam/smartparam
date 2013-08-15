package org.smartparam.engine.test.bean.config;

import javax.annotation.PostConstruct;

/**
 *
 * @author Adam Dubiel
 */
public class DummyPreparableBean {

    private boolean prepared = false;

    @PostConstruct
    public void initialize() {
        prepared = true;
    }

    public boolean isPrepared() {
        return prepared;
    }
}
