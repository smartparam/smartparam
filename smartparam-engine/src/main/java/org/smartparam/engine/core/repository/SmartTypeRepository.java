/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.Type;

/**
 * Klasa zapewnia dostep do systemu typow silnika. Jednoczesnie jest to
 * centralny zarzadca systemu typow. Silnik korzysta z jednej instancji tego
 * providera.
 * <p>
 * Provider mozna skonfigurowac programowo, poprzez metode
 * {@link #registerType(java.lang.String, org.smartparam.engine.core.type.AbstractType)},
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
 *   [bean id="typeProvider" class="org.smartparam.engine.core.config.TypeProvider"]
 *     [property name="typeMap"]
 *       [map]
 *         [entry key="string" value-ref="stringType" /]
 *         [entry key="integer" value-ref="integerType" /]
 *       [/map]
 *     [/property]
 *     [bean id="stringType" class="org.smartparam.engine.types.string.StringType" /]
 *     [bean id="integerType" class="org.smartparam.engine.types.integer.IntegerType" /]
 *   [/bean]
 * </pre>
 *
 * Kazdy typ {@link AbstractType} jest zarejestrowany pod unikalnym kodem.
 *
 * @author Adam Dubiel
 */
public class SmartTypeRepository extends AbstractAnnotationScanningRepository<Type<?>> implements TypeRepository {

    private Logger logger = LoggerFactory.getLogger(SmartTypeRepository.class);

    /**
     * Przechowuje typu pod unikalnymi kodami.
     */
    private Map<String, Type<?>> typeMap = new HashMap<String, Type<?>>();

    /**
     * Rejestruje podany typ i kojarzy go z podanym kodem.
     *
     * @param code kod jednoznacznie reprezentujacy typ (case sensitive)
     * @param type typ rejestrowany pod podanym kodem
     * @throws ParamException jesli podany [code] jest juz zarejestrowany
     */
    public void registerType(String code, Type<?> type) {
        if (typeMap.containsKey(code)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_TYPE_CODE, "other type has been already registered under " + code + " code");
        }
        logger.info("registering type: {} -> {}", code, type.getClass());
        typeMap.put(code, type);
    }

    public Map<String, Type<?>> registeredTypes() {
        return Collections.unmodifiableMap(typeMap);
    }

    /**
     * Zwraca typ zarejestrowany pod podanym kodem (case sensitive).
     *
     * @param code kod typu
     * @return typ zarejestrowany pod tym kodem
     */
    public Type<?> getType(String code) {
        return typeMap.get(code);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamType.class;
    }

    @Override
    protected void handleRegistration(RepositoryObjectKey key, Type<?> objectToRegister) {
        registerType(key.getKey(), objectToRegister);
    }

    /**
     * Setter dla mapy typeMap.
     *
     * @param typeMap mapa
     */
    public void setTypes(Map<String, Type<?>> typeMap) {
        this.typeMap = typeMap;
    }
}
