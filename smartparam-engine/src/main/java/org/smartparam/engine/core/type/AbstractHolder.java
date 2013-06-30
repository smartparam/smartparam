package org.smartparam.engine.core.type;

import java.math.BigDecimal;
import java.util.Date;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * Kontrakt, ktory musi spelniac kazda klasa, ktora reprezentuje wartosci
 * jakiegos typu z wewnetrznego systemu typow silnika.
 * <p>
 * Na przyklad NumberHolder reprezentuje wartosci typu NumberType.
 *
 * <p>
 * Klasa musi implementowac metode {@link #getValue()}, ktora bedzie
 * zwracac javowa reprezentacje typu. Z tej metody moze korzystac uzytkownik
 * modulu parametrycznego.
 *
 * <p>
 * Dodatkowo klasa definiuje metody pomocnicze (convenience methods),
 * z ktorych uzytkownik powinien korzystac jak najczesciej, np:
 * <pre>
 *      int k = getIntegerHolder(...).getInt();
 *      BigDecimal factor = getNumberHolder(...).getBigDecimal();
 *      double factor = getNumberHolder(...).getDouble();
 * </pre>
 * Jesli uzycie ktorejs metody pomocniczej w danym typie nie ma sensu
 * (np. getDouble w DateHolder), metoda powinna rzucic wyjatek
 * oznaczajacy niepoprawne uzycie wartosci parametru.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class AbstractHolder implements Comparable<AbstractHolder> {

    /**
     * Zwraca obiekt javowy bedacy ostateczna wartoscia parametru.
     * <p>
     * Konkretne implementacje moga nadpisywac typ zwracany,
     * na przyklad NumberHolder moze przeslonic metode w taki sposob:
     * <pre>
     * public BigDecimal getValue()
     * </pre>
     *
     * @return ostateczna (unwrapped) wartosc (np. parametru)
     */
    public abstract Object getValue();

    /**
     * Sprawdza, czy wartosc jest interpretowana jako null.
     *
     * @return <tt>true</tt> jesli wartosc jest interpretowana jako null, <tt>false</tt> w przeciwnym przypadku
     */
    public boolean isNull() {
        return getValue() == null;
    }

    /**
     * Sprawdza, czy wartosc jest interpretowana jako rozna od null.
     *
     * @return <tt>true</tt> jesli wartosc jest rozna od null, <tt>false</tt> w przeciwnym przypadku
     */
    public boolean isNotNull() {
        return getValue() != null;
    }

    /**
     * Porownuje <b>zawartosc</b> biezacego holdera z holderem przekazanym w parametrze.
     * Przekazany holder jest rowny z biezacym holderem (w sensie equals), wtedy i tylko wtedy, gdy:
     * <ul>
     * <li>przekazany holder jest dokladnie tej samej klasy co biezacy
     * <li>zawartosc biezacego holdera jest rowna (equals) zawartosci przekazanego holdera lub obie wartosci sa rowne <tt>null</tt>
     * </ul>
     *
     * @param obj przekazny obiekt
     * @return true, jesli oba holdera sa rowne co do zawartosci
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {

            Object v1 = getValue();
            Object v2 = ((AbstractHolder) obj).getValue();

            if (v1 == null) {
                return v2 == null;
            }
            if (v2 != null) {
                return v1.equals(v2);
            }
        }
        return false;
    }

    /**
     * Wylicza hash code dla holdera wedlug wzoru:
     * <pre>
     *   hashcode(holder) := hashcode(holder.getClass()) XOR hashcode(holder.getValue())
     * </pre>
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        Object v = getValue();
        int vhash = v != null ? v.hashCode() : 1;
        return this.getClass().hashCode() ^ vhash;
    }

    /**
     * Metoda powinna informowac, czy wartosci zwracane przez {@link #getValue()}
     * implementuja interface {@link Comparable}.
     * <p>
     * Ta metoda (jako default) zwraca <tt>true</tt>, ale kazdy holder moze ja nadpisac, jesli bedzie taka potrzeba.
     *
     * @return czy wartosci zwracane przez holder sa porownywalne (Comparable)
     */
    public boolean isComparable() {
        return true;
    }

    /**
     * Metoda sluzy do porownywania zawartosci holderow (tej samej klasy).
     * Zwraca wartosc:
     * <ul>
     * <li>-1 gdy biezacy holder jest mniejszy od <tt>o</tt>,
     * <li> 0 gdy biezacy holder jest rowny <tt>o</tt>,
     * <li>+1 gdy biezacy holder jest wiekszy od <tt>o</tt>.
     * </ul>
     * Moze byc uzywana na przyklad przez Matchery przedzialowe.
     *
     * @param o holder tej samej klasy co biezacy
     * @return wyniko porownywania (w sensie interace'u Comparable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(AbstractHolder o) {
        if (isComparable()) {

            Comparable<Object> v1 = (Comparable<Object>) this.getValue();
            Comparable<Object> v2 = (Comparable<Object>) o.getValue();

            if (v1 != null) {
                return v2 != null ? v1.compareTo(v2) : 1;
            } else {
                return v2 != null ? -1 : 0;
            }
        }

        return 0;
    }

    /**
     * Zwraca stringowa reprezentacje holdera w postaci:
     * <pre>
     *   NazwaKlasy[getValue()]
     * </pre>
     * na przyklad:
     * <pre>
     *   StringHolder[ABC]
     *   NumberHolder[3.14]
     * </pre>
     *
     * @return string pokazujacy zawartosc holdera
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(EXPECTED_TOSTR_LEN);
        sb.append(this.getClass().getSimpleName());
        sb.append('[').append(getValue()).append(']');
        return sb.toString();
    }
    /**
     * Oczekiwana maksymalna dlugosc wyniku metody toString, wystarczajaca dla wiekszosci przypadkow.
     */
    private static final int EXPECTED_TOSTR_LEN = 32;


    /**
     * Tworzy wyjatek swiadczacy o niepoprawnym dostepie do wartosci.
     *
     * @param t nazwa typu, ktory probuje pobrac uzytkownik
     * @return wyjatek ParamUsageException
     */
    private SmartParamUsageException unexpectedUsage(String t) {
        return new SmartParamUsageException(
                SmartParamErrorCode.GETTING_WRONG_TYPE,
                "trying to get [" + t + "] value from " + this.getClass());
    }

    /**
     * Zwraca int, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako int
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public int intValue() {
        throw unexpectedUsage("int");
    }

    /**
     * Zwraca long, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako long
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public long longValue() {
        throw unexpectedUsage("long");
    }

    /**
     * Zwraca double, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako double
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public double doubleValue() {
        throw unexpectedUsage("double");
    }

    /**
     * Zwraca boolean, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako boolean
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public boolean booleanValue() {
        throw unexpectedUsage("boolean");
    }

    /**
     * Zwraca Integer, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako Integer
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public Integer getInteger() {
        throw unexpectedUsage("Integer");
    }

    /**
     * Zwraca Long, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako Long
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public Long getLong() {
        throw unexpectedUsage("Long");
    }

    /**
     * Zwraca Double, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako Double
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public Double getDouble() {
        throw unexpectedUsage("Double");
    }

    /**
     * Zwraca Boolean, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako Boolean
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public Boolean getBoolean() {
        throw unexpectedUsage("Boolean");
    }

    /**
     * Zwraca reprezentacje stringowa (toString) obiektu przechowywanego przez dany holder,
     * lub null, jesli przechowywany obiekt jest rowny null.
     * <p>
     * Metoda moze byc przyslaniana w holderach, jesli bedzie taka potrzeba.
     *
     * @return wartosc jako string lub null, jesli holder zawiera null
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana jako string
     */
    public String getString() {
        Object value = getValue();
        return value != null ? value.toString() : null;
    }

    /**
     * Zwraca wartosc jako BigDecimal, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako BigDecimal
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public BigDecimal getBigDecimal() {
        throw unexpectedUsage("BigDecimal");
    }

    /**
     * Zwraca Date, jesli dla danego typu ma to sens.
     *
     * @return wartosc jako Date
     * @throws ParamUsageException jesli wartosc nie moze byc interpretowana w ten sposob
     */
    public Date getDate() {
        throw unexpectedUsage("Date");
    }

}
