package org.smartparam.engine.types.integer;

import java.math.BigDecimal;
import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Klasa reprezentuje wartosci typu {@link IntegerType}.
 * Wewnetrza reprezentacja bazuje na polu <tt>value</tt> typu Long.
 * Wartosc moze byc rowna null.
 * <p>
 * Obiekty tej klasy sa niezmienne (immutable).
 *
 * @see IntegerType
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class IntegerHolder extends AbstractHolder {

    /**
     * Wewnetrzna reprezentacja liczby calkowitej (64 bit, signed).
     */
    private Long value;

    /**
     * Jedyny sposob wypelnienia obiektu wartoscia. Obiekty sa immutable.
     *
     * @param value reprezentowana wartosc.
     */
    public IntegerHolder(Long value) {
        this.value = value;
    }

    /**
     * Zwraca obiekt trzymany przez holder.
     *
     * @return wewnetrzna wartosc
     */
    @Override
    public Long getValue() {
        return value;
    }

    /**
     * Zwraca wartosc jako int. Jesli holder trzyma wartosc null, metoda zwraca 0.
     * Jesli trzymana wartosc jest wieksza od 32 bit signed, zostanie obcieta
     * poprzez rzutowanie na int.
     *
     * @return wartosc zrzutowana na int
     */
    @Override
    public int intValue() {
        return value != null ? value.intValue() : 0;
    }

    /**
     * Zwraca wartosc jako typ prosty. Null zostanie zwrocony jako 0L.
     *
     * @return wartosc jako typ prosty
     */
    @Override
    public long longValue() {
        return value != null ? value : 0;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value != null ? BigDecimal.valueOf(longValue()) : null;
    }

    @Override
    public Integer getInteger() {
        return value != null ? Integer.valueOf(intValue()) : null;
    }

    @Override
    public Long getLong() {
        return value;
    }
}