package org.smartparam.engine.types.string;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Klasa reprezentuje wartosci typu {@link StringType}.
 * Wewnetrza reprezentacja bazuje na polu <tt>value</tt> typu String.
 * Wartosc moze byc rowna null.
 * <p>
 * Obiekty tej klasy sa niezmienne (immutable).
 *
 * @see StringType
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class StringHolder extends AbstractHolder {

    /**
     * Wewnetrzna reprezentacja wartosci.
     */
    private String value;

    /**
     * Jedyny sposob wypelnienia obiektu wartoscia.
     *
     * @param value reprezentowana wartosc.
     */
    public StringHolder(String value) {
        this.value = value;
    }

    /**
     * Zwraca wartosc wewnetrzna.
     *
     * @return wartosc wewnetrzna
     */
    @Override
    public String getValue() {
        return value;
    }
}
