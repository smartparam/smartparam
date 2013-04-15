package org.smartparam.engine.core.cache;

import java.util.Map;
import org.smartparam.engine.model.function.Function;

/**
 * Kontrakt zapewniajacy cache'owanie obiektow funkcji z repozytorium.
 * Silnik parametryczny w wielu metodach pobiera obiektu funkcji
 * na podstawie jej unikalnej nazwy. Do tego celu wykorzystuje
 * implementacje tego wlasnie interfejsu.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface FunctionCache {

    /**
     * Wstawia funkcje <tt>function</tt> pod kluczem <tt>functionName</tt>.
     *
     * @param functionName unikalna nazwa funkcji
     * @param function     funkcja z repozytorium
     */
    void put(String functionName, Function function);

    void putAll(Map<String, Function> functions);

    /**
     * Zwraca funkcje o nazwie <tt>functionName</tt> lub <tt>null</tt>,
     * jesli nie ma w cache'u takiej funkcji.
     *
     * @param functionName nazwa funkcji
     *
     * @return funkcja pobrana z cache'a
     */
    Function get(String functionName);

    /**
     * Usuwa z cache'a funkcje o nazwie <tt>functionName</tt>.
     *
     * @param functionName nazwa funkcji
     */
    void invalidate(String functionName);

    /**
     * Usuwa z cache'a wszystkie funkcje.
     */
    void invalidate();
}
