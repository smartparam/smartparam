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

    protected String matcher;

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
    public String getMatcher() {
        return matcher;
    }

    @Override
    public String getName() {
        return name;
    }
}
