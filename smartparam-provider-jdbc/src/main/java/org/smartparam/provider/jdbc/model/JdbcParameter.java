package org.smartparam.provider.jdbc.model;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameter implements Parameter {

    /**
     * Default value for {@link #arraySeparator} field.
     */
    public static final char DEFAULT_ARRAY_SEPARATOR = ',';

    /**
     * Unique identifier.
     */
    private int id;

    /**
     * Unique parameter name (code).
     */
    private String name;

    private List<Level> levels;

    private Set<ParameterEntry> entries;

    private int inputLevels;

    private boolean nullable;

    private boolean cacheable = true;

    private char arraySeparator = DEFAULT_ARRAY_SEPARATOR;

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    public int getLevelCount() {
        return levels != null ? levels.size() : 0;
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return entries;
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameter#").append(id);
        sb.append('[').append(name);
        sb.append(", levels=").append(getLevelCount());
        sb.append(", inputLevels=").append(getInputLevels());
        sb.append(nullable ? ", nullable" : ", notnull");

        if (!cacheable) {
            sb.append(", nocache");
        }

        sb.append(']');
        return sb.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
