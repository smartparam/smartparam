package org.smartparam.engine.test.mock;

import org.smartparam.engine.model.Level;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class LevelMock implements Level {

    private String name;

    private String levelCreator;

    private String type;

    private boolean array;

    private String matcherCode;

    public LevelMock() {
    }

    public LevelMock(String levelCreator, String type, boolean array, String matcherCode) {
        this.levelCreator = levelCreator;
        this.type = type;
        this.array = array;
        this.matcherCode = matcherCode;
    }

    @Override
    public String getName() {
        return name;
    }

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

    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public void setMatcherCode(String matcherCode) {
        this.matcherCode = matcherCode;
    }
}
