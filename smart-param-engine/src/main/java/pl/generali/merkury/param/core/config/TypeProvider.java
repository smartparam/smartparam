package pl.generali.merkury.param.core.config;

import java.util.HashMap;
import java.util.Map;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.exception.ParamException.ErrorCode;
import pl.generali.merkury.param.core.type.AbstractType;

/**
 * Klasa zapewnia dostep do systemu typow silnika.
 * Jednoczesnie jest to centralny zarzadca systemu typow.
 * Silnik korzysta z jednej instancji tego providera.
 * <p>
 * Provider mozna skonfigurowac programowo, poprzez metode
 * {@link #registerType(java.lang.String, pl.generali.merkury.param.core.type.AbstractType)},
 * lub deklaratywnie z uzyciem metody {@link #setTypeMap(java.util.Map)}.
 * <p>
 * Konfiguracja programowa:
 * <pre>
 *  TypeProvider tp = new TypeProvider();
 *  tp.registerType("string", new StringType());
 *  tp.registerType("integer", new IntegerType());
 *  ...
 *  ParamEngine engine = new ParamEngine();
 *  engine.setTypeProvider(tp);
 * </pre>
 *
 * Przyklad konfiguracji deklaratywnej z wykorzystaniem Springa:
 * <pre>
 *   [bean id="typeProvider" class="pl.generali.merkury.param.core.config.TypeProvider"]
 *     [property name="typeMap"]
 *       [map]
 *         [entry key="string" value-ref="stringType" /]
 *         [entry key="integer" value-ref="integerType" /]
 *       [/map]
 *     [/property]
 *     [bean id="stringType" class="pl.generali.merkury.param.types.string.StringType" /]
 *     [bean id="integerType" class="pl.generali.merkury.param.types.integer.IntegerType" /]
 *   [/bean]
 * </pre>
 *
 * Kazdy typ {@link AbstractType} jest zarejestrowany pod unikalnym kodem.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class TypeProvider {

    /**
     * Przechowuje typu pod unikalnymi kodami.
     */
    private Map<String, AbstractType<?>> typeMap = new HashMap<String, AbstractType<?>>();

    /**
     * Rejestruje podany typ i kojarzy go z podanym kodem.
     *
     * @param code kod jednoznacznie reprezentujacy typ (case sensitive)
     * @param type typ rejestrowany pod podanym kodem
     * @throws ParamException jesli podany [code] jest juz zarejestrowany
     */
    public void registerType(String code, AbstractType<?> type) {
        if (typeMap.containsKey(code)) {
            throw new ParamException(ErrorCode.NON_UNIQUE_TYPE_CODE, "Such code is already registered: " + code);
        }
        typeMap.put(code, type);
    }

    /**
     * Zwraca typ zarejestrowany pod podanym kodem (case sensitive).
     *
     * @param code kod typu
     * @return typ zarejestrowany pod tym kodem
     */
    public AbstractType<?> getType(String code) {
        return typeMap.get(code);
    }

    /**
     * Setter dla mapy typeMap.
     *
     * @param typeMap mapa
     */
    public void setTypeMap(Map<String, AbstractType<?>> typeMap) {
        this.typeMap = typeMap;
    }
}
