package org.smartparam.engine.test.mock;

import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterEntryMock implements ParameterEntry {

    private String[] levels;

    private String value;

    private String function;

    public ParameterEntryMock() {
    }

    public ParameterEntryMock(String... levels) {
        this.levels = levels;
    }

    @Override
    public String[] getLevels() {
        return levels;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getFunction() {
        return function;
    }

    public void setLevels(String[] levels) {
        this.levels = levels;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
