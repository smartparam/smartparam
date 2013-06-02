package org.smartparam.repository.fs.model;

import org.smartparam.engine.model.Level;
import org.smartparam.mgmt.model.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSLevel implements Level, EditableLevel {

    private String label;

    private String levelCreator;

    private String type;

    private boolean array;

    private String matcherCode;

    @Override
    public String getLevelCreator() {
        return levelCreator;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public String getMatcherCode() {
        return matcherCode;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setMatcherCode(String matcherCode) {
        this.matcherCode = matcherCode;
    }
}
