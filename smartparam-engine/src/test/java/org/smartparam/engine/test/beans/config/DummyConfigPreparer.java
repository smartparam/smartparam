package org.smartparam.engine.test.beans.config;

import org.smartparam.engine.config.ConfigPreparer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class DummyConfigPreparer extends ConfigPreparer<DummyConfigBean> {

    @Override
    protected DummyConfigBean createInstance(DummyConfigBean config) {
        return new DummyConfigBean();
    }

    @Override
    protected void customizeNewDefaultValue(DummyConfigBean config, Object object) {
    }
}
