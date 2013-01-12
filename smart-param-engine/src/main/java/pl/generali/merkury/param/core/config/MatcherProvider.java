package pl.generali.merkury.param.core.config;

import java.util.HashMap;
import java.util.Map;
import pl.generali.merkury.param.core.index.Matcher;

/**
 * Klasa udostepnia (zarejestrowane uprzednio) matchery.
 * Matchery mozna pobierac na podstawie unikalnego kodu.
 *
 * @see Matcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MatcherProvider {

    /**
     * Mapuje kod matchera na obiekt matchera.
     */
    private Map<String, Matcher> matcherMap = new HashMap<String, Matcher>();

    /**
     * Rejestruje matcher <tt>matcher</tt> pod kodem <tt>code</tt>.
     * Rejestracja nie jest thread-safe, wiec powinna byc wykonywana
     * wylacznie podczas inicjalizacji aplikacji.
     *
     * @param code    kod matchera
     * @param matcher obiekt matchera
     */
    public void registerMatcher(String code, Matcher matcher) {
        matcherMap.put(code, matcher);
    }

    /**
     * Zwraca matcher zarejstrowany pod kodem <tt>code</tt>
     * lub <tt>null</tt>, jesli nie ma takiego matchera.
     *
     * @param code kod matchera
     * @return matcher zarejestrowany pod tym kodem
     */
    public Matcher getMatcher(String code) {
        return matcherMap.get(code);
    }

    /**
     * Ustawia cala mape zarejestrowanych matcherow.
     *
     * @param matcherMap mapa matcherow
     */
    public void setMatcherMap(Map<String, Matcher> matcherMap) {
        this.matcherMap = matcherMap;
    }
}
