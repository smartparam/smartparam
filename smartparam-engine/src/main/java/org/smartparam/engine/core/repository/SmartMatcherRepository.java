package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.RepositoryObjectKey;
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
public class SmartMatcherRepository extends AbstractAnnotationScanningRepository<Matcher> implements MatcherRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Mapuje kod matchera na obiekt matchera.
     */
    private Map<String, Matcher> matcherMap = new HashMap<String, Matcher>();

    /**
     * Rejestruje matcher <tt>matcher</tt> pod kodem <tt>code</tt>. Rejestracja
     * nie jest thread-safe, wiec powinna byc wykonywana wylacznie podczas
     * inicjalizacji aplikacji.
     *
     * @param code kod matchera
     * @param matcher obiekt matchera
     */
    public void registerMatcher(String code, Matcher matcher) {
        logger.info("registering matcher: {} -> {}", code, matcher.getClass());
        matcherMap.put(code, matcher);
    }

    public Map<String, Matcher> registeredMatchers() {
        return Collections.unmodifiableMap(matcherMap);
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
    protected void handleRegistration(RepositoryObjectKey key, Matcher objectToRegister) {
        registerMatcher(key.getKey(), objectToRegister);
    }

    /**
     * Ustawia cala mape zarejestrowanych matcherow.
     *
     * @param matcherMap mapa matcherow
     */
    public void setMatchers(Map<String, Matcher> matcherMap) {
        this.matcherMap = matcherMap;
    }
}
