package org.smartparam.engine.core.index;

import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 * Kontrakt, ktory spelnia kazdy <i>matcher</i> zarejestrowany
 * w ramach {@link org.smartparam.engine.core.config.MatcherProvider}.
 * <p>
 * Rola matchera jest sprawdzac, czy dana wartosc <b>pasuje</b>
 * do wzorca zapisanego w danej komorce macierzy parametru.
 *
 * @see org.smartparam.engine.matchers.BetweenMatcher
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
    <T extends AbstractHolder> boolean matches(String value, String pattern, Type<T> type);
}
