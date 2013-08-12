package org.smartparam.engine.model.editable;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.SimpleParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleEditableParameter extends SimpleParameter implements EditableParameter {

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

    @Override
    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
