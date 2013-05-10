package org.smartparam.engine.types.bool;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Klasa reprezentuje wartosci typu {@link BooleanType}.
 * Wewnetrza reprezentacja bazuje na polu <tt>value</tt> typu Boolean.
 * Wartosc moze byc rowna null.
 * <p>
 * Obiekty tej klasy sa niezmienne (immutable).
 *
 * @see BooleanType
 *
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class BooleanHolder extends AbstractHolder {

    /**
     * Wewnetrzna reprezentacja wartosci.
     */
    private Boolean value;

    /**
     * Jedyny sposob wypelnienia obiektu wartoscia.
     *
     * @param value reprezentowana wartosc.
     */
    public BooleanHolder(Boolean value) {
        this.value = value;
    }

    /**
     * Zwraca wartosc wewnetrzna.
     *
     * @return wartosc wewnetrzna
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * Zwraca wartosc jako typ prosty. Null zostanie zwrocony jako <tt>false</tt>.
     *
     * @return wartosc jako typ prosty
     */
    @Override
    public boolean booleanValue() {
        return value != null ? value : false;
    }

    @Override
    public Boolean getBoolean() {
        return value;
    }
}
