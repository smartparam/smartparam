package org.smartparam.engine.model;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleLevel implements Level {

    protected String name;

    protected String levelCreator;

    protected String type;

    protected boolean array;

    protected String matcherCode;

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
    public String getName() {
        return name;
    }
}
