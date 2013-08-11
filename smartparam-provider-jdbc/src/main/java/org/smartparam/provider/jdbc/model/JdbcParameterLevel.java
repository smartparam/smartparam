package org.smartparam.provider.jdbc.model;

import org.smartparam.engine.model.Level;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterLevel implements Level {

    private int id;

    private int parameterId;

    private int orderNo;

    private String name;

    private String type;

    private String matcher;

    private String levelCreator;

    private boolean array;

    public int getOrderNo() {
        return orderNo;
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
    public String getMatcher() {
        return matcher;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JdbcParameterLevel[");
        sb.append("id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", type=").append(type);

        if (matcher != null) {
            sb.append(", matcher=").append(matcher);
        }

        if (array) {
            sb.append(" array");
        }

        sb.append(", label=").append(name);

        sb.append(']');
        return sb.toString();
    }
}
