package org.smartparam.engine.test.beans.config;

import java.util.Map;
import org.smartparam.engine.config.ConfigElement;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class DummyConfigBean {

    @ConfigElement(Object.class)
    private Object optionalField;

    private Object mandatoryField;

    @ConfigElement(Map.class)
    private Map<String, String> mapField;

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
