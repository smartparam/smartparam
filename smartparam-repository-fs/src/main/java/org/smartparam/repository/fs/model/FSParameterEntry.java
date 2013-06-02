
package org.smartparam.repository.fs.model;

import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.mgmt.model.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParameterEntry implements ParameterEntry, EditableParameterEntry {

    private String[] levels;
    
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

    @Override
    public void setLevels(String[] levels) {
        this.levels = levels;
    }

}
