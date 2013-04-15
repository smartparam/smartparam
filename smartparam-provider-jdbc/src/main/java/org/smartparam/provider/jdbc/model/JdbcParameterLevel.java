package org.smartparam.provider.jdbc.model;

import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.Level;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterLevel implements Level {

    private int id;

    private int parameterId;

    private int orderNo;

    private String label;

    private String type;

    private String matcher;

    private boolean array;

    @Override
    public int getOrderNo() {
        return orderNo;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Function getLevelCreator() {
        return null;
        //TODO #ph fix
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
        return matcher;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setLabel(String label) {
        this.label = label;
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

        sb.append(", label=").append(label);

        sb.append(']');
        return sb.toString();
    }
}
