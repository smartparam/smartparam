package org.smartparam.engine.model.editable;

import org.smartparam.engine.model.SimpleParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleEditableParameterEntry extends SimpleParameterEntry implements EditableParameterEntry {

    @Override
    public void setLevels(String[] levels) {
        this.levels = levels;
    }
}
