package org.smartparam.engine.core.cache;

import org.smartparam.engine.model.function.Function;

/**
 * Implementacja {@link FunctionCache} oparata na wspolbieznej wersji HashMapy.
 * Funkcje zapisane w tym cache'u sie nie przedawniaja az do wywolania metody
 * {@link #invalidate(java.lang.String)}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MapFunctionCache extends MapCache<Function> implements FunctionCache {
}
