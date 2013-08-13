package org.smartparam.engine.core.engine;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.util.Printer;

/**
 * Represents single row of matrix returned from parameter querying. Immutable.
 * All method accept 1-based arguments, meaning to get first value all getValue(1).
 * Each method returning value can throw {@link SmartParamException} with:
 *
 * * {@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS} when trying to access wrong index
 * * {@link SmartParamErrorCode#GETTING_WRONG_TYPE} when trying to get wrong type from position
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MultiValue {

    /**
     * Values held, each entry is either AbstractHolder or AbstractHolder[].
     */
    private Object[] values;

    /**
     * Keeps iteration state, used to power next* methods.
     */
    private int last;

    public MultiValue(Object[] values) {
        this.values = values;
    }

    /**
     * Returns value stored at position.
     *
     * @param position value position, 1-based
     * @return value holder, throws exception if array is stored
     */
    public AbstractHolder getValue(int position) {
        Object obj = get(position);

        if (obj instanceof AbstractHolder) {
            return (AbstractHolder) obj;
        }

        throw new SmartParamException(
                SmartParamErrorCode.GETTING_WRONG_TYPE,
                "Expecting AbstractHolder but found " + printClass(obj) + " at position " + position);
    }

    /**
     * @param position 1-based
     * @return string representation of value
     */
    public String getString(int position) {
        return getValue(position).getString();
    }

    /**
     * @param position 1-based
     * @return big decimal value, if supported by holder
     */
    public BigDecimal getBigDecimal(int position) {
        return getValue(position).getBigDecimal();
    }

    /**
     * @param position 1-based
     * @return date value, if supported by holder
     */
    public Date getDate(int position) {
        return getValue(position).getDate();
    }

    /**
     * @param position 1-based
     * @return integer value, if supported by holder
     */
    public Integer getInteger(int position) {
        return getValue(position).getInteger();
    }

    /**
     * @param position 1-based
     * @return long value, if supported by holder
     */
    public Long getLong(int position) {
        return getValue(position).getLong();
    }

    /**
     * Parses string value as enum entry, using {@link Enum#valueOf(java.lang.Class, java.lang.String) } method.
     *
     * @param <T> enum type
     * @param position 1-based
     * @param enumClass enum class
     * @return enum value
     */
    public <T extends Enum<T>> T getEnum(int position, Class<T> enumClass) {
        String code = getString(position);
        return code != null ? codeToEnum(code, enumClass) : null;
    }

    private <T extends Enum<T>> T codeToEnum(String code, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, code);

        } catch (IllegalArgumentException e) {
            throw new SmartParamException(SmartParamErrorCode.GETTING_WRONG_TYPE, e, "Requested enum has no such constant: " + code);
        }
    }

    /**
     * Return array of values stored at position. Should be used if parameter
     * level contained list of values ({@link org.smartparam.engine.model.Level#isArray()}.
     * This string list is split into array of values using separator defined at
     * parameter level ({@link org.smartparam.engine.model.Parameter#getArraySeparator()}).
     * Type of each value holder in array is the same, defined by level type.
     *
     * @param position 1-based
     * @return array of value holders of same type
     */
    public AbstractHolder[] getArray(int position) {
        Object obj = get(position);

        if (obj instanceof AbstractHolder[]) {
            return (AbstractHolder[]) obj;
        }

        throw new SmartParamException(
                SmartParamErrorCode.GETTING_WRONG_TYPE,
                "Expecting AbstractHolder[] but found " + printClass(obj) + " at position " + position);
    }

    /**
     * Return array of unwrapped objects, this is a raw representation of
     * contents of AbstractHolders from MultiValue object.
     * Each array element is either value of {@link AbstractHolder#getValue() }
     * if level stores single value or array of {@link AbstractHolder#getValue() }
     * if level stores an array.
     */
    public Object[] unwrap() {
        Object[] result = new Object[values.length];

        for (int i = 0; i < values.length; i++) {
            Object val = values[i];

            // jesli i-ty element jest holderem
            if (val instanceof AbstractHolder) {
                AbstractHolder cell = (AbstractHolder) val;
                result[i] = cell.getValue();
            }

            // jesli i-ty element jest tablica holderow
            if (val instanceof AbstractHolder[]) {
                AbstractHolder[] cell = (AbstractHolder[]) val;
                Object[] array = new Object[cell.length];
                for (int j = 0; j < cell.length; j++) {
                    array[j] = cell[j].getValue();
                }
                result[i] = array;
            }
        }

        return result;
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego <b>jako tablice String[]</b>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie moze byc interpretowana jako tablica stringow
     */
    public String[] getStringArray(int k) {
        AbstractHolder[] array = getArray(k);
        String[] result = new String[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getString();
        }
        return result;
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego <b>jako tablice Date[]</b>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie moze byc interpretowana jako tablica dat
     */
    public Date[] getDateArray(int k) {
        AbstractHolder[] array = getArray(k);
        Date[] result = new Date[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getDate();
        }
        return result;
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego <b>jako tablice Integer[]</b>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie moze byc interpretowana jako tablica integerow
     */
    public Integer[] getIntegerArray(int k) {
        AbstractHolder[] array = getArray(k);
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getInteger();
        }
        return result;
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego <b>jako tablice BigDecimal[]</b>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie moze byc interpretowana jako tablica obiektow BigDecimal
     */
    public BigDecimal[] getBigDecimalArray(int k) {
        AbstractHolder[] array = getArray(k);
        BigDecimal[] result = new BigDecimal[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getBigDecimal();
        }
        return result;
    }

    /**
     * Zwraca wartosc k-tego poziomu.
     *
     * @param k numer poziomu wyjsciowego
     *
     * @return AbstractHolder albo AbstractHolder[]
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     */
    private Object get(int k) {
        if (k >= 1 && k <= values.length) {
            return values[k - 1];
        }
        throw new SmartParamUsageException(
                SmartParamErrorCode.INDEX_OUT_OF_BOUNDS,
                "Getting element from non-existing position: " + k);
    }

    /**
     * Zwraca wartosci poziomow jako Stringi.
     * Kazdy k-ty element musi byc typu {@link AbstractHolder}.
     *
     * @return tablica stringow odczytanych z poszczegolnych elementow
     */
    public String[] asStrings() {
        String[] array = new String[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getString(i + 1);
        }
        return array;
    }

    /**
     * Zwraca wartosci poziomow jako obiekty BigDecimal.
     * Kazdy k-ty element musi byc typu {@link AbstractHolder}.
     *
     * @return tablica BigDecimali odczytanych z poszczegolnych elementow
     */
    public BigDecimal[] asBigDecimals() {
        BigDecimal[] array = new BigDecimal[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getBigDecimal(i + 1);
        }
        return array;
    }

    /**
     * Zwraca krotka nazwe klasy obiektu.
     *
     * @param obj obiekt
     *
     * @return krotka nazwa klasy lub <tt>null</tt>, gdy obiekt rowny </tt>null</tt>
     */
    private String printClass(Object obj) {
        return obj != null ? obj.getClass().getSimpleName() : null;
    }

    @Override
    public String toString() {
        return Printer.print(values, "MultiValue");
    }

    /**
     * Zwraca zawartosc w jednej linii.
     *
     * @return zawartosc obiektu zapisana w jednej linii
     */
    public String toStringInline() {
        Object[] rawValues = unwrap();
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < rawValues.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            Object v = rawValues[i];

            if (v instanceof Object[]) {
                sb.append(Arrays.toString((Object[]) v));
            } else {
                sb.append(v);
            }
        }

        sb.append(']');
        return sb.toString();
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli jest k poziomow wyjsciowych, a metoda jest uzyta k+1 raz
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc poziomu nie jest typu {@link AbstractHolder}
     */
    public AbstractHolder nextValue() {
        return getValue(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako String.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako String
     */
    public String nextString() {
        return getString(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako BigDecimal.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako BigDecimal
     */
    public BigDecimal nextBigDecimal() {
        return getBigDecimal(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako Date.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Date
     */
    public Date nextDate() {
        return getDate(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako Integer.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Integer
     */
    public Integer nextInteger() {
        return getInteger(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako Long.
     * Kolejne wywolania tej metody iteruja przez poziomy wyjsciowe.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Long
     */
    public Long nextLong() {
        return getLong(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako obiekt <tt>enum</tt> oczekiwanej klasy <tt>enumClass</tt>.
     *
     * @return wartosc k-tego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli enumClass nie ma pola o takiej nazwie
     */
    public <T extends Enum<T>> T nextEnum(Class<T> enumClass) {
        return getEnum(++last, enumClass);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public AbstractHolder[] nextArray() {
        return getArray(++last);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice String</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public String[] nextStringArray() {
        return getStringArray(++last);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice BigDecimal</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws SmartParamException errorcode={@link SmartParamErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public BigDecimal[] nextBigDecimalArray() {
        return getBigDecimalArray(++last);
    }

    public int size() {
        return values != null ? values.length : 0;
    }
}
