package org.smartparam.editor.backend.form;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterForm {

    private String name;

    private String label;

    private String description;

    private int inputLevels;

    private boolean cacheable;

    private boolean mutlivalue;

    private boolean nullable;

    private boolean array;

    private char arraySeparator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInputLevels() {
        return inputLevels;
    }

    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isMutlivalue() {
        return mutlivalue;
    }

    public void setMutlivalue(boolean mutlivalue) {
        this.mutlivalue = mutlivalue;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public char getArraySeparator() {
        return arraySeparator;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
