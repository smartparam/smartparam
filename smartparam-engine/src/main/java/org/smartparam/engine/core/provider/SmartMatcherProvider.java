package org.smartparam.engine.core.provider;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.index.Matcher;

/**
 * Klasa udostepnia (zarejestrowane uprzednio) matchery. Matchery mozna pobierac
 * na podstawie unikalnego kodu.
 *
 * @see Matcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartMatcherProvider extends AbstractProvider<Matcher> implements MatcherProvider {

    /**
     * Mapuje kod matchera na obiekt matchera.
     */
    private Map<String, Matcher> matcherMap = new HashMap<String, Matcher>();

    public SmartMatcherProvider() {
        super();
    }

    public SmartMatcherProvider(boolean scanAnnotations, PackageList packagesToScan) {
        super(scanAnnotations, packagesToScan);
    }

    /**
     * Rejestruje matcher <tt>matcher</tt> pod kodem <tt>code</tt>. Rejestracja
     * nie jest thread-safe, wiec powinna byc wykonywana wylacznie podczas
     * inicjalizacji aplikacji.
     *
     * @param code kod matchera
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

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamMatcher.class;
    }

    @Override
    protected void handleRegistration(String objectCode, Matcher objectToRegister) {
        registerMatcher(objectCode, objectToRegister);
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
