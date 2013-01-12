package pl.generali.merkury.param.types.string;

import pl.generali.merkury.param.core.type.AbstractType;

/**
 * Klasa definiuje typ stringowy, ktory moze zostac wlaczony
 * do systemu typow rozpoznawanych przez silnik.
 * <p>
 * Typ stringowy przechowuje wszystkie wartosci w obiekcie {@link StringHolder}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class StringType extends AbstractType<StringHolder> {

    /**
     * Zamienia obiekt holdera na <tt>String</tt>.
     *
     * @param holder obiekt holdera
     * @return wartosc holdera podana wprost
     */
    @Override
    public String encode(StringHolder holder) {
        return holder.getValue();
    }

    /**
     * Zamienia string na obiekt holdera.
     *
     * @param text string
     * @return obiekt holdera
     */
    @Override
    public StringHolder decode(String text) {
        return new StringHolder(text);
    }

    /**
     * Konwertuje dowolny obiekt (np. zwrocony przez funkcje) na obiekt holdera.
     * Wartosc stringowa holdera bedzie rowna wynikowi metody <tt>toString</tt> obiektu.
     * Argument rowny <tt>null</tt> zostanie zamieniony na StringHolder reprezentujacy null.
     * <p>
     * Na przyklad:
     * <pre>
     *   convert( new Integer(17)  );        // StringHolder.getValue() : "17"
     *   convert( new Float(1.0/3) );        // StringHolder.getValue() : "0.33333334"
     *   convert( null );                    // StringHolder.getValue() : null
     *   convert( null );                    // StringHolder.isNull()   : true
     * </pre>
     *
     * @param obj dowolny obiekt java lub null
     * @return obiekt holdera
     */
    @Override
    public StringHolder convert(Object obj) {
        return new StringHolder(obj != null ? obj.toString() : null);
    }

    @Override
    public StringHolder[] newArray(int size) {
        return new StringHolder[size];
    }
}
