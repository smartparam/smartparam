package org.smartparam.engine.test.bean.config;

import java.util.Map;
import org.smartparam.engine.config.ConfigElement;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class TestConfigBean {

    @ConfigElement(Object.class)
    private Object optionalField;

    private Object mandatoryField;

    @ConfigElement(Map.class)
    private Map<String, String> mapField;

    public static TestConfigBean withMandatoryField() {
        TestConfigBean configBean = new TestConfigBean();
        configBean.setMandatoryField(new Object());
        return configBean;
    }

    public static TestConfigBean withEmptyMandatoryField() {
        TestConfigBean configBean = new TestConfigBean();
        return configBean;
    }

    public Object getOptionalField() {
        return optionalField;
    }

    public void setOptionalField(Object optionalField) {
        this.optionalField = optionalField;
    }

    public Object getMandatoryField() {
        return mandatoryField;
    }

    public void setMandatoryField(Object mandatoryField) {
        this.mandatoryField = mandatoryField;
    }

    public Map<String, String> getMapField() {
        return mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
    }
}
