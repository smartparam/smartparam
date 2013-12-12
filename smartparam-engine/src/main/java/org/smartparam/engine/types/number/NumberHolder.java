/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private final BigDecimal value;

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
