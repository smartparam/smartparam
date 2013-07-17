package org.smartparam.engine.types.integer;

import org.smartparam.engine.annotations.ParamType;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.EngineUtil;

/**
 * Klasa definiuje typ calkowitoliczbowy, ktory moze zostac wlaczony
 * do systemu typow rozpoznawanych przez silnik.
 * <p>
 * Typ ten przechowuje wartosci calkowite w obiekcie {@link IntegerHolder},
 * ktory moze reprezentowac:
 * <ul>
 * <li>liczby calkowite z przedzialu od -9223372036854775808 do 9223372036854775807 (64 bit, signed)
 * <li>wartosc <tt>null</tt>
 * </ul>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@ParamType("integer")
public class IntegerType implements Type<IntegerHolder> {

    /**
     * Zamienia obiekt holdera na <tt>String</tt>.
     *
     * @param holder obiekt holdera
     * @return stringowa reprezentacja holdera lub null, jesli wartosc holdera jest null
     */
    @Override
    public String encode(IntegerHolder value) {
        Long v = value.getValue();
        return v != null ? v.toString() : null;
    }

    /**
     * Zamienia string na obiekt holdera.
     * Moze rzucic wyjatek, jesli string nie reprezentuje liczby,
     * ktora da sie przechowac w obiekcie {@link IntegerHolder}.
     * String rowny <tt>null</tt> lub majacy wylacznie biale znaki zamieniany
     * jest na <tt>IntegerHolder(null)</tt>.
     *
     * @param text string reprezentujacy liczbe calkowita
     * @return obiekt holdera
     * @throws NumberFormatException jesli string nie reprezentuje liczby typu {@link Long}
     */
    @Override
    public IntegerHolder decode(String text) {
        Long value = EngineUtil.hasText(text) ? Long.valueOf(text.trim()) : null;
        return new IntegerHolder(value);
    }

    /**
     * Jesli podany obiekt (obj) reprezentuje typ calkowitoliczbowy,
     * ktory mozna bezstratnie zapisac w zmiennej typu Long,
     * to metoda skonwertuje ten obiekt na {@link IntegerHolder}.
     * <p>
     * Jesli obj jest stringiem, ktory mozna bezstratnie sparsowac
     * jako liczbe long, to ten string rowniez zostanie skonwertowany.
     * Jesli string nie parsuje sie do longa, metoda rzuci wyjatek.
     * <p>
     * Typy java, ktore sa konwertowalne na IntegerHolder:
     * <ul>
     * <li>Long
     * <li>Integer
     * <li>Short
     * <li>Byte
     * <li>null
     * <li>String, jesli mozna go sparsowac na Long
     * </ul>
     * Argument rowny <tt>null</tt> zostanie skonwertowany na IntegerHolder reprezentujacy null.
     * <p>
     * Na przyklad:
     * <pre>
     *   convert( new Long(17)  );      // IntegerHolder.getValue() : Long(17)
     *   convert( 17 );                 // IntegerHolder.getValue() : Long(17)
     *   convert( null );               // IntegerHolder.getValue() : null
     *   convert( "17" );               // IntegerHolder.getValue() : Long(17)
     *   convert( 0.11 );               // throws IllegalArgumentException
     *   convert( "9A" );               // throws NumberFormatException
     * </pre>
     *
     * @param obj dowolny obiekt java lub null
     * @return obiekt holdera
     * @throws IllegalArgumentException jesli przekazany obiekt nie jest konwertowalny na IntegerHolder
     * @throws NumberFormatException    jesli obiekt jako string nie jest parsowalny na Long
     */
    @Override
    public IntegerHolder convert(Object obj) {
        if (obj instanceof Long || obj instanceof Integer || obj instanceof Short || obj instanceof Byte) {
            Number n = (Number) obj;
            return new IntegerHolder(n.longValue());
        }

        if (obj == null) {
            return new IntegerHolder(null);
        }

        if (obj instanceof String) {
            return decode((String) obj);
        }

        throw new IllegalArgumentException("conversion not supported for: " + obj.getClass());
    }

    @Override
    public IntegerHolder[] newArray(int size) {
        return new IntegerHolder[size];
    }
}
