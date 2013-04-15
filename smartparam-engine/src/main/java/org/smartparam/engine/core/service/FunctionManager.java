package org.smartparam.engine.core.service;

import org.smartparam.engine.model.function.Function;

/**
 * Kontrakt, ktory musi spelniac kazda klasa udostepniajaca
 * funkcje z repozytorium na podstawie jej unikalnej nazwy.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface FunctionManager {

    Object invokeFunction(String name, Object... args);

    Object invokeFunction(Function function, Object... args);
}
