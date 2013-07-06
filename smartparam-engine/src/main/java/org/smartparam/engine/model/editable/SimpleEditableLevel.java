package org.smartparam.engine.model.editable;

import org.smartparam.engine.model.SimpleLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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
    public void setMatcherCode(String matcherCode) {
        this.matcherCode = matcherCode;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
