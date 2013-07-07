package org.smartparam.engine.core.repository;

import java.util.Map;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.annotations.scanner.TypeScanner;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartTypeRepository implements TypeRepository, TypeScanningRepository {

    private MapRepository<Type<?>> innerRepository = new MapRepository<Type<?>>(Type.class);

    @Override
    public void scanAnnotations(TypeScanner scanner) {
        Map<RepositoryObjectKey, Type<?>> types = scanner.scanTypes(SmartParamType.class);
        innerRepository.registerAll(types);
    }

    @Override
    public Type<?> getType(String code) {
        return innerRepository.getItem(code);
    }

    @Override
    public void register(String code, Type<?> type) {
        innerRepository.registerUnique(code, type);
    }

    @Override
    public Map<String, Type<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, Type<?>> typeMap) {
        innerRepository.registerAllUnordered(typeMap);
    }
}
