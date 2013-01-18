package org.smartparam.engine.types.number;

import java.math.BigDecimal;
import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Klasa reprezentuje wartosci typu {@link NumberType}.
 * Wewnetrza reprezentacja bazuje na polu <tt>value</tt> typu BigDecimal.
 * Dzieki temu przechowuje w sposob <b>bezstratny</b> kazda liczbe,
 * ktora mozna zapisac jako String.
 * <p>
 * Obiekty tej klasy sa niezmienne (immutable).
 *
 * @see NumberType
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class NumberHolder extends AbstractHolder {

    /**
     * Wewnetrzna reprezentacja liczby.
     */
    private BigDecimal value;

    /**
     * Jedyny sposob wypelnienia obiektu wartoscia. Obiekty sa immutable.
     *
     * @param value reprezentowana wartosc.
     */
    public NumberHolder(BigDecimal value) {
        this.value = value;
    }

    /**
     * Zwraca obiekt trzymany przez holder.
     *
     * @return wewnetrzna wartosc
     */
    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value;
    }

    @Override
    public int intValue() {
        return value != null ? value.intValue() : 0;
    }

    @Override
    public long longValue() {
        return value != null ? value.longValue() : 0;
    }

    @Override
    public double doubleValue() {
        return value != null ? value.doubleValue() : 0.0;
    }

    @Override
    public Double getDouble() {
        return value != null ? Double.valueOf(doubleValue()) : null;
    }

    @Override
    public Integer getInteger() {
        return value != null ? Integer.valueOf(intValue()) : null;
    }

    @Override
    public Long getLong() {
        return value != null ? Long.valueOf(longValue()) : null;
    }

    /**
     * Zwraca reprezentacje dziesietna liczby w zwyklej notacji.
     * Metoda dziala nieco inaczej niz {@link BigDecimal#toString()},
     *
     * @see BigDecimal#toPlainString()
     * @return
     */
    @Override
    public String getString() {
        return value != null ? value.toPlainString() : null;
    }
}
