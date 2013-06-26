package org.smartparam.engine.test.bean.config;

import org.smartparam.engine.config.ConfigPreparer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class TestConfigPreparer extends ConfigPreparer<TestConfigBean> {

    @Override
    protected TestConfigBean createInstance(TestConfigBean config) {
        return new TestConfigBean();
    }

    @Override
    protected void customizeNewDefaultValue(TestConfigBean config, Object object) {
    }
}
