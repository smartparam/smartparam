package org.smartparam.engine.core.engine;

import java.util.Arrays;
import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedEntry {

    //todo ph: par 2 clean, toString prepared entry
    private String[] levels = null;

    private String value = null;

    //todo ph: par 0, pprovider zamienia nazwe na funkcje (pojo) i umieszcza w cache'u
    private Function function = null;

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String[] getLevels() {
        return levels;
    }

    public void setLevels(String[] levels) {
        this.levels = trimRight(levels);
        //todo ph: par 1 preparedEntry: level=null -> ""
    }

    String[] trimRight(String[] array) {
        if (array == null) {
            return EMPTY_ARRAY;
        }

        int len = array.length;
        while (len > 0 && array[len - 1] == null) {
            --len;
        }

        // len to dlugosc tablicy z pominieciem koncowych null (jesli wystepuja)
        return len < array.length ? Arrays.copyOf(array, len) : array;
    }

    public String getLevel(int k) {
        return (k >= 1 && k <= levels.length) ? levels[k - 1] : null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    private static final String[] EMPTY_ARRAY = {};

}
