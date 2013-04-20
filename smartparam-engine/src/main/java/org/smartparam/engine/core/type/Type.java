package org.smartparam.engine.core.type;

/**
 * Klasa reprezentuje kontrakt, ktory musi spelniac kazdy typ
 * obslugiwany przez silnik parametryczny.
 * <p>
 * Silnik parametryczny posiada swoj wewnetrzny system typow.
 * Typy definiuje uzytkownik poprzez skonfigurowanie obiektu
 * TypeProvider.
 * <p>
 * Klasa ta reprezentuje typ. Natomiast wartosci zgodne z tym typem
 * reprezentuje skojarzona z typem podklasa {@link AbstractHolder}.
 * Na przyklad StringType przechowuje swoje wartosci w obiektach
 * klasy StringHolder.
 * <p>
 * Silnik dostarcza wiele domyslnych typow:
 * <ul>
 * <li>StringType + StringHolder
 * <li>NumberType + NumberHolder
 * <li>StringArrayType + StringArrayHolder
 * <li>...
 * </ul>
 * Ponadto uzytkownik moze zdefiniowac wlasne poprzez implementacje
 * pary: AbstractType + AbstractHolder.
 *
 * @param <T> klasa przechowujaca wartosci typu
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface Type<T extends AbstractHolder> {

    /**
     * Zamienia wartosc typu {@code holder} na string.
     * Kodowanie powinno byc bezstratne oraz jednoznacznie odwracalne, tzn:
     * <pre>
     * decode( encode(X) ) == X
     * </pre>
     *
     * @param holder wartosc typu reprezentowana przez holder
     * @return zamieniona na string wartosc
     */
    String encode(T holder);

    /**
     * Zamienia reprezentacje stringowa na reprezentacje wewnetrzna typu,
     * czyli na obiekt odpowiedniego holdera.
     * Dekodowanie jest operacja odwrotna do kodowania, tzn:
     * <pre>
     * encode( decode(str) ) == str
     * </pre>
     *
     * @param text tekstowa reprezentacja wartosci
     * @return wewnetrzna reprezentacja wartosci (holder)
     */
    T decode(String text);

    /**
     * Konwertuje dowolny obiekt javowy na wartosc typu.
     *
     * @param obj dowolny obiekt
     * @return reprezentacja wewnetrzna typu (holder), ktora jest
     * odpowiednikiem przekazanego obiektu (w sensie typu T)
     */
    T convert(Object obj);

    /**
     * Tworzy <b>nowa</b> i niewypelniona tablice holderow o rozmiarze <tt>size</tt>.
     *
     * @param size rozmiar potrzebnej tablicy
     * @return nowa, niewypelniona tablica o rozmiarze <tt>size</tt>
     */
    T[] newArray(int size);
}
