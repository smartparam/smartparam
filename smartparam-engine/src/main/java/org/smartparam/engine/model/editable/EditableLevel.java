package org.smartparam.engine.model.editable;

import org.smartparam.engine.model.Level;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface EditableLevel extends Level {

    void setName(String name);

    void setLevelCreator(String levelCreator);

    void setType(String type);

    void setMatcher(String matcher);

    void setArray(boolean array);
}
