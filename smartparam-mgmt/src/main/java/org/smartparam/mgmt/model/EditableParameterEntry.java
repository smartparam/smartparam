package org.smartparam.mgmt.model;

import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface EditableParameterEntry extends ParameterEntry {

    void setLevels(String[] levels);

}
