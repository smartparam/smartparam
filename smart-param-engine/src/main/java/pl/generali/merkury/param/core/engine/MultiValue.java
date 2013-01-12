package pl.generali.merkury.param.core.engine;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang3.ClassUtils;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.exception.ParamException.ErrorCode;
import pl.generali.merkury.param.core.exception.ParamUsageException;
import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.util.Printer;

/**
 * Obiekt reprezentuje wartosc parametru typu <i>multivalue</i>.
 * Innymi slowy reprezentuje wartosc zapisana na wielu poziomach.
 * <p>
 *
 * Dla przykladu, jesli parametr definiuje 5 poziomow, w tym 2 wejsciowe i 3 wyjsciowe, wowczas wartoscia
 * parametru jest obiekt MultiValue obejmujacy 3 poziomy wyjsciowe.
 * <p>
 *
 * Obiekt MultiValue udostepnia metody dostepowe dla kazdego (k-tego) poziomu wyjsciowego.
 * Metody dostepowe sa 2 rodzajow:
 * <ol>
 * <li> podstawowe - zwracajace obiekty zwrocone wprost przez silnik,
 * <li> convenience methods - pobierajace wartosci docelowe: int, BigDecimal, Date, itp.
 * </ol>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MultiValue {

    /**
     * Numer pierwszej kolumny.
     */
    public static final int FIRST_COLUMN = 1;

    /**
     * Numer drugiej kolumny.
     */
    public static final int SECOND_COLUMN = 2;

    /**
     * Numer trzeciej kolumny.
     */
    public static final int THIRD_COLUMN = 3;

    /**
     * Numer czwartej kolumny.
     */
    public static final int FOURTH_COLUMN = 4;

    /**
     * Numer piatej kolumny.
     */
    public static final int FIFTH_COLUMN = 5;

    /**
     * Numer szostej kolumny.
     */
    public static final int SIXTH_COLUMN = 6;

    /**
     * Numer siodmej kolumny.
     */
    public static final int SEVETH_COLUMN = 7;

    /**
     * Numer osmej kolumny.
     */
    public static final int EIGHTH_COLUMN = 8;

    /**
     * Numer dziewiatej kolumny.
     */
    public static final int NINTH_COLUMN = 9;

    /**
     * Numer dziesiatej kolumny.
     */
    public static final int TENTH_COLUMN = 10;

    /**
     * Wartosci poziomow wyjsciowych.
     * Kazdy element to obiekt <tt>AbstractHolder</tt> albo tablica <tt>AbstractHolder[]</tt>.
     */
    private Object[] values;

    private int last;

    /**
     * Jedyny sposob utworzenia obiektu. Obiekt jest immutable.
     *
     * @param values wartosci poziomow wyjsciowych
     */
    public MultiValue(Object[] values) {
        this.values = values;
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie jest typu {@link AbstractHolder}
     */
    public AbstractHolder getValue(int k) {
        Object obj = get(k);

        if (obj instanceof AbstractHolder) {
            return (AbstractHolder) obj;
        }

        throw new ParamException(
                ErrorCode.GETTING_WRONG_TYPE,
                "Expecting AbstractHolder but found " + printClass(obj) + " at position " + k);
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako String.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako String
     */
    public String getString(int k) {
        return getValue(k).getString();
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako BigDecimal.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako BigDecimal
     */
    public BigDecimal getBigDecimal(int k) {
        return getValue(k).getBigDecimal();
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako Date.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Date
     */
    public Date getDate(int k) {
        return getValue(k).getDate();
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako Integer.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Integer
     */
    public Integer getInteger(int k) {
        return getValue(k).getInteger();
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako Long.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Long
     */
    public Long getLong(int k) {
        return getValue(k).getLong();
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego jako obiekt <tt>enum</tt> oczekiwanej klasy <tt>enumClass</tt>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli enumClass nie ma pola o takiej nazwie
     */
    public <T extends Enum<T>> T getEnum(int k, Class<T> enumClass) {
        String code = getString(k);
        return code != null ? codeToEnum(code, enumClass) : null;
    }

    /**
     * Zamienia kod na odpowiedni enum.
     *
     * @param <T>       parametr klasy enuma
     * @param code      kod stalej wewnatrz enuma
     * @param enumClass klasa enuma
     *
     * @return obiekt enuma klasy <tt>enumClass</tt> o kodzie <tt>code</tt>
     */
    private <T extends Enum<T>> T codeToEnum(String code, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, code);

        } catch (IllegalArgumentException e) {
            throw new ParamException(ErrorCode.GETTING_WRONG_TYPE, e, "Requested enum has no such constant: " + code);
        }
    }

    /**
     * Zwraca wartosc k-tego poziomu wyjsciowego <b>jako tablice</b>.
     *
     * @param k numer poziomu (numerowanie od 1)
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
     *                             jesli wartosc k-tego poziomu nie jest tablica obiektow {@link AbstractHolder}
     */
    public AbstractHolder[] getArray(int k) {
        Object obj = get(k);

        if (obj instanceof AbstractHolder[]) {
            return (AbstractHolder[]) obj;
        }

        throw new ParamException(
                ErrorCode.GETTING_WRONG_TYPE,
                "Expecting AbstractHolder[] but found " + printClass(obj) + " at position " + k);
    }

    /**
     * Zwraca tablice wartosci wyluskanych z holderow:
     * <ol>
     * <li> Kazdy i-ty element typu AbstractHolder zwracany jest jako wartosc AbstractHolder.getValue().
     * <li> Kazdy i-ty element typu AbstractHolder[] zwracany jest jako tablica Object[] wartosci AbstractHolder.getValue().
     * </ol>
     *
     * @return tablica obiektow wewnetrznie trzymanych przez holdery
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli k jest niepoprawnym numerem poziomu wyjsciowego
     */
    private Object get(int k) {
        if (k >= 1 && k <= values.length) {
            return values[k - 1];
        }
        throw new ParamUsageException(
                ErrorCode.INDEX_OUT_OF_BOUNDS,
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
        return ClassUtils.getShortClassName(obj, null);
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS},
     *                             jesli jest k poziomow wyjsciowych, a metoda jest uzyta k+1 raz
     *
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE},
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako String
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako BigDecimal
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Date
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Integer
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
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest interpretowalna jako Long
     */
    public Long nextLong() {
        return getLong(++last);
    }

    /**
     * Zwraca wartosc <b>kolejnego</b> poziomu wyjsciowego jako obiekt <tt>enum</tt> oczekiwanej klasy <tt>enumClass</tt>.
     *
     * @return wartosc k-tego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli enumClass nie ma pola o takiej nazwie
     */
    public <T extends Enum<T>> T nextEnum(Class<T> enumClass) {
        return getEnum(++last, enumClass);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public AbstractHolder[] nextArray() {
        return getArray(++last);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice String</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public String[] nextStringArray() {
        return getStringArray(++last);
    }

    /**
     * Zwraca wartosc kolejnego poziomu wyjsciowego <b>jako tablice BigDecimal</b>.
     *
     * @return wartosc kolejnego poziomu
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli iteracja wyszla poza ostatni poziom wyjsciowy
     * @throws ParamException      errorcode={@link ErrorCode#GETTING_WRONG_TYPE}, jesli wartosc nie jest tablica obiektow {@link AbstractHolder}
     */
    public BigDecimal[] nextBigDecimalArray() {
        return getBigDecimalArray(++last);
    }

    public int getLastColumnCounter() {
        return last;
    }

    public void resetLastCounter() {
        last = 0;
    }
}
