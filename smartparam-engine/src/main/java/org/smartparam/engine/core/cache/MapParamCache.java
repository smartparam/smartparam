package org.smartparam.engine.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.smartparam.engine.core.engine.PreparedParameter;

/**
 * Implementacja {@link ParamCache} oparata na wspolbieznej wersji HashMapy.
 * Funkcje zapisane w tym cache'u sie nie przedawniaja az do wywolania
 * metody {@link #invalidate(java.lang.String)}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MapParamCache implements ParamCache {

    /**
     * Bezpieczna watkowo mapa, sluzaca jako trwaly cache.
     */
    private Map<String, PreparedParameter> map = new ConcurrentHashMap<String, PreparedParameter>();

    @Override
    public void put(String paramName, PreparedParameter pp) {
        map.put(paramName, pp);
    }

    @Override
    public PreparedParameter get(String paramName) {
        return map.get(paramName);
    }

    @Override
    public void invalidate(String paramName) {
        map.remove(paramName);
    }

    @Override
    public void invalidate() {
        map.clear();
    }

}
