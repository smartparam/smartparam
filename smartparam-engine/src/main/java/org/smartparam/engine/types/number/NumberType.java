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
import java.math.BigInteger;
import org.smartparam.engine.annotated.annotations.ParamType;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.EngineUtil;

/**
 * Klasa definiuje typ liczbowy.
 * Przechowuje wartosci liczbowe w obiekcie {@link NumberHolder}, ktory moze reprezentowac:
 * <ul>
 * <li>dowolna liczbe rzeczywista, ktora ma reprezentacje dziesietna (BigDecimal)
 * <li>wartosc <tt>null</tt>
 * </ul>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@ParamType("number")
public class NumberType implements Type<NumberHolder> {

    /**
     * Zamienia obiekt holdera na <tt>String</tt>.
     *
     * @param holder obiekt holdera
     *
     * @return stringowa reprezentacja holdera lub null, jesli wartosc holdera jest null
     */
    @Override
    public String encode(NumberHolder value) {
        return value.getString();
    }

    /**
     * Zamienia string na obiekt holdera.
     * Moze rzucic wyjatek, jesli string nie reprezentuje liczby,
     * ktora da sie przechowac w obiekcie {@link NumberHolder}.
     * String rowny <tt>null</tt> lub majacy wylacznie biale znaki zamieniany
     * jest na <tt>NumberHolder(null)</tt>.
     *
     * @param text string reprezentujacy liczbe
     *
     * @return obiekt holdera
     *
     * @throws NumberFormatException jesli string nie reprezentuje liczby typu {@link BigDecimal}
     */
    @Override
    public NumberHolder decode(String text) {
        BigDecimal value = EngineUtil.hasText(text) ? parse(text) : null;
        return new NumberHolder(value);
    }

    @Override
    public NumberHolder convert(Object obj) {

        if (obj instanceof Number) {
            Number n = (Number) obj;
            BigDecimal result;

            if (n instanceof Double || n instanceof Float) {
                result = new BigDecimal(n.doubleValue());
            } else if (n instanceof BigDecimal) {
                result = (BigDecimal) n;
            } else if (n instanceof BigInteger) {
                result = new BigDecimal((BigInteger) n);
            } else {
                result = new BigDecimal(n.longValue());
            }

            return new NumberHolder(result);
        }

        if (obj == null) {
            return new NumberHolder(null);
        }

        if (obj instanceof String) {
            return decode((String) obj);
        }

        throw new IllegalArgumentException("conversion not supported for: " + obj.getClass());
    }

    @Override
    public NumberHolder[] newArray(int size) {
        return new NumberHolder[size];
    }

    private BigDecimal parse(String str) {
        return new BigDecimal(EngineUtil.trimAllWhitespace(str).replace(',', '.'));
    }
}
