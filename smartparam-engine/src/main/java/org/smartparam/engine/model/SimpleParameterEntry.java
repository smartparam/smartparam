package org.smartparam.engine.model;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleParameterEntry implements ParameterEntry {

    protected String[] levels;

    private String value;

    private String function;

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
}
