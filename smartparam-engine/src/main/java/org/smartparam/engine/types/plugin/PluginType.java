package org.smartparam.engine.types.plugin;

import org.smartparam.engine.annotations.ParamType;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.util.EngineUtil;

/**
 * Klasa definiuje typ, ktorego wartosciami sa poszczegolne pluginy.
 * Przechowuje nazwy pluginow/funkcji w obiektach {@link PluginHolder}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Deprecated
@ParamType("plugin")
public class PluginType implements Type<PluginHolder> {

    //todo #ph 0 will be removed

    /**
     * Zamienia obiekt holdera na <tt>String</tt>.
     * Innymi slowy zwraca nazwe funkcji reprezentowanej przez podany PluginHolder
     *
     * @param holder obiekt holdera
     *
     * @return nazwa funkcji pluginowej
     */
    @Override
    public String encode(PluginHolder holder) {
        return holder.getString();
    }

    /**
     * Zamienia string na obiekt holdera. Traktuje argument <tt>text</tt>
     * jako nazwe funkcji pluginowej. Tekst pusty lub zawierajacy wylacznie biale znaki
     * traktuje jako brak funkcji pluginowej.
     *
     * @param text string bedacy nazwa funkcji
     *
     * @return obiekt holdera
     */
    @Override
    public PluginHolder decode(String text) {
        String functionName = EngineUtil.hasText(text) ? text : null;
        return new PluginHolder(functionName);
    }

    @Override
    public PluginHolder convert(Object obj) {
        if (obj instanceof Function) {
            return new PluginHolder(((Function) obj).getName());
        }

        if (obj instanceof String) {
            return decode((String) obj);
        }

        if (obj == null) {
            return new PluginHolder(null);
        }

        throw new IllegalArgumentException("conversion not supported for: " + obj.getClass());
    }

    @Override
    public PluginHolder[] newArray(int size) {
        return new PluginHolder[size];
    }

}
