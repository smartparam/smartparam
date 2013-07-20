package org.smartparam.engine.types.bool;

import org.smartparam.engine.annotations.ParamType;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.EngineUtil;

/**
 * Klasa definiuje typ logiczny, ktory moze zostac wlaczony
 * do systemu typow rozpoznawanych przez silnik.
 * <p>
 * Typ ten przechowuje wartosci logiczne w obiekcie {@link BooleanHolder},
 * ktory moze reprezentowac:
 * <ul>
 * <li>wartosc logiczna <tt>false</tt>
 * <li>wartosc logiczna <tt>true</tt>
 * <li>wartosc <tt>null</tt>
 * </ul>
 *
 * @author Przemek Hertel
 * @since 0.2.0
 */
@ParamType("boolean")
public class BooleanType implements Type<BooleanHolder> {

    public String encode(BooleanHolder holder) {
        return String.valueOf(holder.getBoolean());
    }

    public BooleanHolder decode(String text) {
        Boolean value = EngineUtil.hasText(text) ? parse(text) : null;
        return new BooleanHolder(value);
    }

    public BooleanHolder convert(Object obj) {

        if (obj instanceof Boolean) {
            return new BooleanHolder((Boolean) obj);
        }

        if (obj instanceof String) {
            return decode((String) obj);
        }

        if (obj == null) {
            return new BooleanHolder(null);
        }

        throw new IllegalArgumentException("conversion not supported for: " + obj.getClass());
    }

    public BooleanHolder[] newArray(int size) {
        return new BooleanHolder[size];
    }

    private Boolean parse(String text) {
        return Boolean.valueOf(text);
    }
    
    //TODO #ph: finish boolean: 1) convert 2) parse 3) attributes

    /*
     * attr             default
     * formatTrue       "true"  - literaly uzywane przez encode
     * formatFalse      "false"
     * 
     * parseTrue        true, t, yes, y, 1
     * parseFalse       false, f, no, n, 0
     * ignoreCase       true
     * parseUnknownAsEx true
     * parseUnknownAs   false (:boolean)
     */
    
}
