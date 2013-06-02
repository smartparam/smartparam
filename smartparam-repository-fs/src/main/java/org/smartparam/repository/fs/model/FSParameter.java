package org.smartparam.repository.fs.model;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.mgmt.model.EditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParameter implements Parameter, EditableParameter {

    private String name;

    private String type;

    private List<Level> levels;

    private int inputLevels;

    private Set<ParameterEntry> entries;

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
    public List<? extends Level> getLevels() {
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

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    @Override
    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    @Override
    public void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
