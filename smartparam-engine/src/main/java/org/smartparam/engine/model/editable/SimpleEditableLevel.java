package org.smartparam.engine.model.editable;

import org.smartparam.engine.model.SimpleLevel;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleEditableLevel extends SimpleLevel implements EditableLevel {

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    @Override
    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setArray(boolean array) {
        this.array = array;
    }
}
