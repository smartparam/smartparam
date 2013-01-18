package org.smartparam.engine.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.smartparam.engine.model.Function;

/**
 * Implementacja {@link FunctionCache} oparata na wspolbieznej wersji HashMapy.
 * Funkcje zapisane w tym cache'u sie nie przedawniaja az do wywolania
 * metody {@link #invalidate(java.lang.String)}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MapFunctionCache implements FunctionCache {

    /**
     * Bezpieczna watkowo mapa, sluzaca jako trwaly cache.
     */
    private Map<String, Function> map = new ConcurrentHashMap<String, Function>();

    @Override
    public void put(String functionName, Function function) {
        map.put(functionName, function);
    }

    @Override
    public Function get(String functionName) {
        return map.get(functionName);
    }

    @Override
    public void invalidate(String functionName) {
        map.remove(functionName);
    }

    @Override
    public void invalidate() {
        map.clear();
    }
}
