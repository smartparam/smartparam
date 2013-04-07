package org.smartparam.provider.jdbc.model;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.provider.TypeProvider;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;

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

    /**
     * Label or caption legible for human. Mainly for GUI purposes.
     */
    private String label;

    /**
     * Detailed parameter description. Mainly for GUI purposes.
     */
    private String description;

    /**
     * Code of parameter type if parameter is single valued.
     * This code has to be compatible to smartparam type system provided by {@link TypeProvider} object.
     */
    private String type;

    private List<JdbcParameterLevel> levels;

    private Set<JdbcParameterEntry> entries;

    private boolean multivalue;

    private int inputLevels;

    private boolean nullable;

    private boolean cacheable = true;

    private boolean array;

    private char arraySeparator = DEFAULT_ARRAY_SEPARATOR;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<JdbcParameterLevel> getLevels() {
        return levels;
    }

    @Override
    public Level getLevel(int levelNumber) {
        return levels.get(levelNumber);
        //TODO #ph remove getLevel(int ix)
    }

    @Override
    public int getLevelCount() {
        return levels.size();
        //TODO #ph remove from interface
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<JdbcParameterEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean isArchive() {
        return false;
        //TODO #ph remove isArchive
    }

    @Override
    public boolean isArray() {
        return array;
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
    public boolean isMultivalue() {
        return multivalue;
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
        sb.append(", type=").append(type);
        sb.append(", levels=").append(getLevelCount());
        sb.append(", inputLevels=").append(getInputLevels());
        sb.append(nullable ? ", nullable" : ", notnull");

        if (multivalue) {
            sb.append(", multivalue");
        }
        if (array) {
            sb.append(", array");
        }
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLevels(List<JdbcParameterLevel> levels) {
        this.levels = levels;
    }

    public void setEntries(Set<JdbcParameterEntry> entries) {
        this.entries = entries;
    }

    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
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

    public void setArray(boolean array) {
        this.array = array;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
