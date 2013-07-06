package org.smartparam.engine.model;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleParameter implements Parameter {

    protected String name;

    protected String type;

    protected List<Level> levels;

    protected int inputLevels;

    protected Set<ParameterEntry> entries;

    protected boolean array;

    protected char arraySeparator;

    protected boolean cacheable;

    protected boolean multivalue;

    protected boolean nullable;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public boolean isMultivalue() {
        return multivalue;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }
}
