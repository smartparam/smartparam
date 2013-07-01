package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartTypeRepository extends AbstractAnnotationScanningRepository<Type<?>> implements TypeRepository {

    private MapRepository<Type<?>> innerRepository = new MapRepository<Type<?>>(Type.class);

    @Override
    public void register(String code, Type<?> type) {
        innerRepository.registerUnique(code, type);
    }

    @Override
    public Map<String, Type<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public Type<?> getType(String code) {
        return innerRepository.getItem(code);
    }

    @Override
    public void setItems(Map<String, Type<?>> typeMap) {
        innerRepository.setItemsUnordered(typeMap);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamType.class;
}

    @Override
    protected void handleRegistration(RepositoryObjectKey key, Type<?> objectToRegister) {
        register(key.getKey(), objectToRegister);
    }
}
