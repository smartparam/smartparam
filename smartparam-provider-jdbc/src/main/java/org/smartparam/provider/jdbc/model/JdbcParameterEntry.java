package org.smartparam.provider.jdbc.model;

import java.util.Arrays;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterEntry implements ParameterEntry {

    private int id;

    private int parameterId;

    private String[] levels;

    private String value;

    @Override
    public String[] getLevels() {
        return levels;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getFunction() {
        //TODO #ph fix: change interface to getFunction:string
        return null;
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

    public void setLevels(String[] levels) {
        this.levels = levels;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JdbcParameterEntry[#").append(id);
        sb.append(' ');
        sb.append(Arrays.toString(levels));
        sb.append(" v=").append(value);
        sb.append(']');
        return sb.toString();
    }
}
