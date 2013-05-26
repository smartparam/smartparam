package org.smartparam.mgmt.model;

import org.smartparam.engine.model.Level;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface EditableLevel extends Level {

    String getLabel();
    
    void setLabel(String label);

    void setLevelCreator(String levelCreator);

    void setType(String type);

    void setMatcherCode(String matcherCode);
}
