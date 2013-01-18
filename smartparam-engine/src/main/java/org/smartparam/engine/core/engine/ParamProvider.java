package org.smartparam.engine.core.engine;

import java.util.List;

/**
 * Interface, ktory musi spelniac kazda klasa
 * udostepniajaca przygotowane (prepared) parametry.
 * <p>
 * Zwracane parametry zawieraja pelna informacje,
 * czyli metadane parametru oraz macierz w postaci indeksu.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamProvider {

    /**
     * Zwraca przygotowany obiekt parametru o nazwie <tt>paramName</tt>.
     * Jesli nie ma parametru o takiej nazwie - zwraca <tt>null</tt>.
     *
     * @param paramName nazwa parametru
     * @return przygotowany parametr (zawiera m.in. zbudowany indeks) lub <tt>null</tt>, jesli nie ma takiego parametru
     */
    PreparedParameter getPreparedParameter(String paramName);

    List<PreparedEntry> findEntries(String paramName, String[] levelValues);
}
