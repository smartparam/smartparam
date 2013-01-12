package pl.generali.merkury.param.types.plugin;

import pl.generali.merkury.param.core.type.AbstractHolder;

/**
 * Klasa reprezentuje wartosci typu {@link PluginType}.
 * Wewnetrza reprezentacja bazuje na polu <tt>value</tt> (String)
 * przechowujacym unikalna (w ramach repozytorium) nazwe funkcji.
 * Wartosc moze byc rowna null.
 * <p>
 * Obiekty tej klasy sa niezmienne (immutable).
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PluginHolder extends AbstractHolder {

    /**
     * Nazwa funkcji pluginowej.
     */
    private String functionName;

    /**
     * Jedyny sposob wypelnienia obiektu wartoscia.
     *
     * @param value nazwa funkcji
     */
    public PluginHolder(String functionName) {
        this.functionName = functionName;
    }

    /**
     * Zwraca nazwe funkcji.
     *
     * @return nazwa funkcji
     */
    @Override
    public Object getValue() {
        return functionName;
    }

    /**
     * Zwraca nazwe funkcji.
     *
     * @return nazwa funkcji
     */
    @Override
    public String getString() {
        return functionName;
    }
}
