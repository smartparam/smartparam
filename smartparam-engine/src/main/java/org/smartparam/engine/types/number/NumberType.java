package org.smartparam.engine.types.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.smartparam.engine.core.type.AbstractType;
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
public class NumberType extends AbstractType<NumberHolder> {

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
