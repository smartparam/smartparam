package org.smartparam.engine.test.mock;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterMock implements Parameter {

    private String name;

    private String type;

    private List<Level> levels;

    private Set<ParameterEntry> entries;

    private int inputLevels;

    private boolean array;

    private char arraySeparator;

    private boolean cacheable;

    private boolean multivalue;

    private boolean nullable;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
