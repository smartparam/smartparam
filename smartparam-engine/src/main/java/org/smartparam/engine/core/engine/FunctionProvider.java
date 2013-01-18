package org.smartparam.engine.core.engine;

import org.smartparam.engine.model.Function;

/**
 * Kontrakt, ktory musi spelniac kazda klasa udostepniajaca
 * funkcje z repozytorium na podstawie jej unikalnej nazwy.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface FunctionProvider {

    /**
     * Zwraca funkcje o nazwie <tt>name</tt>.
     * Jesli nie ma takiej funkcji w repozytorium, rzuca wyjatek.
     *
     * @param name nazwa funkcji
     *
     * @return funkcja z repozytorium
     */
    Function getFunction(String name);
}
