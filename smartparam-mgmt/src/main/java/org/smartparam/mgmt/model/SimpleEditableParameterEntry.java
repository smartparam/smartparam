package org.smartparam.mgmt.model;

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
