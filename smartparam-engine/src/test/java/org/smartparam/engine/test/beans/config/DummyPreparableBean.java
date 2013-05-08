package org.smartparam.engine.test.beans.config;

import javax.annotation.PostConstruct;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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
