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
