package org.smartparam.engine.config;

import org.junit.Before;
import org.junit.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;
import org.smartparam.engine.core.exception.SmartParamConfigException;
import org.smartparam.engine.test.bean.config.TestConfigBean;
import org.smartparam.engine.test.bean.config.TestConfigPreparer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigPreparerTest {

    private TestConfigPreparer configPreparer;

    @Before
    public void setUp() {
        this.configPreparer = new TestConfigPreparer();
    }

    @Test
    public void shouldInjectDefaultValuesForEmptyOptionalField() {
        // given
        TestConfigBean config = TestConfigBean.withMandatoryField();

        // when
        TestConfigBean preparedConfig = configPreparer.getPreparedConfig(config);

        // then
        assertThat(preparedConfig.getOptionalField()).isNotNull();
    }

    @Test
    public void shouldInjectDefaultValuesForEmptyOptionalCollectionField() {
        // given
        TestConfigBean config = TestConfigBean.withMandatoryField();

        // when
        TestConfigBean preparedConfig = configPreparer.getPreparedConfig(config);

        // then
        assertThat(preparedConfig.getMapField()).isNotNull();
    }

    @Test
    public void shouldFailWhenMandatoryFieldIsNull() {
        // given
        TestConfigBean config = TestConfigBean.withEmptyMandatoryField();

        // when
        catchException(configPreparer).getPreparedConfig(config);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamConfigException.class);
    }

    @Test
    public void shouldSkipInsertingDefaultValueOnNonEmptyOptionalField() {
        // given
        TestConfigBean config = TestConfigBean.withMandatoryField();
        Object optionalObject = new Object();
        config.setOptionalField(optionalObject);

        // when
        TestConfigBean preparedConfig = configPreparer.getPreparedConfig(config);

        // then
        assertThat(preparedConfig.getOptionalField()).isSameAs(optionalObject);
    }

}
