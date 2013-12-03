/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.annotated.repository;

import java.util.Map;
import org.smartparam.engine.annotated.annotations.ParamType;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.annotated.repository.TypeScanningRepository;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.type.TypeRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningTypeRepository implements TypeRepository, TypeScanningRepository {

    private MapRepository<Type<?>> innerRepository = new MapRepository<Type<?>>(Type.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner initializer) {
        Map<RepositoryObjectKey, Type<?>> types = scanner.scanTypes(ParamType.class);
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
