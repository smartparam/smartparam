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
import org.smartparam.engine.util.RepositoryHelper;

public class SmartTypeRepository extends AbstractAnnotationScanningRepository<Type<?>> implements TypeRepository {

    private Logger logger = LoggerFactory.getLogger(SmartTypeRepository.class);

    private Map<String, Type<?>> typeMap = new HashMap<String, Type<?>>();

    @Override
    public void register(String code, Type<?> type) {
        if (typeMap.containsKey(code)) {
            throw new SmartParamException(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE, "other type has been already registered under " + code + " code");
        }
        logger.info("registering type: {} -> {}", code, type.getClass());
        typeMap.put(code, type);
    }

    @Override
    public Map<String, Type<?>> registeredItems() {
        return Collections.unmodifiableMap(typeMap);
    }

    @Override
    public Type<?> getType(String code) {
        return typeMap.get(code);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamType.class;
    }

    @Override
    protected void handleRegistration(RepositoryObjectKey key, Type<?> objectToRegister) {
        register(key.getKey(), objectToRegister);
    }

    @Override
    public void setItems(Map<String, Type<?>> typeMap) {
        RepositoryHelper.registerItems(this, typeMap);
    }
}
