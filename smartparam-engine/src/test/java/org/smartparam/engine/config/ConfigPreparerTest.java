package org.smartparam.engine.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.SmartParamConfigException;
import org.smartparam.engine.test.beans.config.DummyConfigBean;
import org.smartparam.engine.test.beans.config.DummyConfigPreparer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigPreparerTest {

    private DummyConfigPreparer configPreparer;

    @Before
    public void initialize() {
        this.configPreparer = new DummyConfigPreparer();
    }
    
    @Test
    public void testPreparation_allOptionalDefaults() {
        DummyConfigBean config = new DummyConfigBean();
        Object mandatoryObject = new Object();
        config.setMandatoryField(mandatoryObject);

        DummyConfigBean preparedConfig = configPreparer.getPreparedConfig(config);

        assertNotNull(preparedConfig.getOptionalField());
        assertNotNull(preparedConfig.getMapField());

        assertSame(mandatoryObject, preparedConfig.getMandatoryField());
    }

    @Test(expected = SmartParamConfigException.class)
    public void testPreparation_emptyRequiredField() {
        DummyConfigBean config = new DummyConfigBean();
        configPreparer.getPreparedConfig(config);
    }

    @Test
    public void testPreparation_providedOptionalFieldShouldNotBeOverriden() {
        DummyConfigBean config = new DummyConfigBean();
        Object mandatoryObject = new Object();
        Object optionalObject = new Object();

        config.setMandatoryField(mandatoryObject);
        config.setOptionalField(optionalObject);

        DummyConfigBean preparedConfig = configPreparer.getPreparedConfig(config);

        assertSame(optionalObject, preparedConfig.getOptionalField());
    }

}
