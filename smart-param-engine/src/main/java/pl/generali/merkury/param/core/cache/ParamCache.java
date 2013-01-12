package pl.generali.merkury.param.core.cache;

import pl.generali.merkury.param.core.engine.PreparedParameter;

/**
 * Kontrakt zapewniajacy cache'owanie przygotowanych parametrow ({@link PreparedParameter}).
 * Silnik parametryczny pobiera obiekty PreparedParameter na podstawie nazwy.
 * Do tego celu wykorzystuje implementacje tego wlasnie interfejsu.
 * <p>
 *
 * Implementacja cache'a musi byc <b>thread-safe</b>.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamCache {

    /**
     * Wstawia parametr <tt>pp</tt> pod kluczem <tt>paramName</tt>.
     *
     * @param paramName unikalna nazwa parametru
     * @param pp        przygotowany parametr
     */
    void put(String paramName, PreparedParameter pp);

    /**
     * Zwraca parametr o nazwie <tt>paramName</tt> lub <tt>null</tt>,
     * jesli nie ma w cache'u takiego parametru.
     *
     * @param paramName nazwa parametru
     *
     * @return parametr pobrany z cache'a
     */
    PreparedParameter get(String paramName);

    /**
     * Usuwa z cache'a parametr o nazwie <tt>paramName</tt>.
     *
     * @param paramName nazwa parametru
     */
    void invalidate(String paramName);

    /**
     * Usuwa z cache'a wszystkie parametry.
     */
    void invalidate();
}
