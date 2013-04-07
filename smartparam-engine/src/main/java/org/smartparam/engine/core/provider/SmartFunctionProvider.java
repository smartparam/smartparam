package org.smartparam.engine.core.provider;

import javax.annotation.PostConstruct;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.loader.FunctionLoader;
import org.smartparam.engine.model.Function;

/**
 * Service Provider, ktory dostarcza funkcje z repozytorium o zadanej nazwie.
 * Pobiera funkcje przy pomocy loadera ({@link FunctionLoader}), ktorego
 * zadaniem jest fizyczne wczytani funkcji z bazy danych. Wczytana funkcja jest
 * cache'owana przy pomocy {@link FunctionCache}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartFunctionProvider implements FunctionProvider {

    /**
     * Loader, ktory fizycznie wczytuje obiekt funkcji.
     */
    private FunctionLoader loader = null;

    /**
     * Cache, w ktorym zapamietywana sa wczytane funkcje.
     */
    private FunctionCache cache = null;

    @PostConstruct
    public void initializeProviders() {
        if (cache == null) {
            cache = new MapFunctionCache();
        }
    }

    @Override
    public Function getFunction(String name) {

        Function f = cache.get(name);

        if (f == null) {
            f = loader.load(name);

            if (f == null) {
                throw new SmartParamDefinitionException(SmartParamErrorCode.UNKNOWN_FUNCTION, "Unknown function: " + name);
            }

            cache.put(name, f);
        }

        return f;
    }

    /**
     * Setter dla cache.
     *
     * @param cache cache.
     */
    public void setCache(FunctionCache cache) {
        this.cache = cache;
    }

    /**
     * Setter dla loadera.
     *
     * @param loader loader
     */
    public void setLoader(FunctionLoader loader) {
        this.loader = loader;
    }
}
