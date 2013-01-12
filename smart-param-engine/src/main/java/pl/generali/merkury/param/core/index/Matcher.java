package pl.generali.merkury.param.core.index;

import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.core.type.AbstractType;

/**
 * Kontrakt, ktory spelnia kazdy <i>matcher</i> zarejestrowany
 * w ramach {@link pl.generali.merkury.param.core.config.MatcherProvider}.
 * <p>
 * Rola matchera jest sprawdzac, czy dana wartosc <b>pasuje</b>
 * do wzorca zapisanego w danej komorce macierzy parametru.
 *
 * @see pl.generali.merkury.param.matchers.BetweenMatcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface Matcher {

    /**
     * Sprawdza, czy podana wartosc <tt>value</tt> pasuje do wzorca <tt>pattern</tt>
     * zapisanego w komorce wiersza parametru (ParameterEntry).
     *
     * @param <T>     typ holdera parametru komorki (typ poziomu)
     * @param value   dopasowywana wartosc
     * @param pattern wzorzec zapisany w komorce
     * @param type    typ komorki (zgodny z systemem typow)
     *
     * @return zwraca <tt>true</tt>, jesli value pasuje do wzorca, <tt>false</tt> w przeciwnym przypadku
     */
    <T extends AbstractHolder> boolean matches(String value, String pattern, AbstractType<T> type);
}
