/*
 * Copyright 2014 Adam Dubiel.
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
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.annotations.ParamMatcherType;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.matcher.MatcherType;
import org.smartparam.engine.core.matcher.MatcherTypeRepository;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.matchers.type.SimpleMatcherType;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningMatcherTypeRepository implements MatcherTypeRepository, TypeScanningRepository {

    private final MatcherType<?> defaultDecoder = new SimpleMatcherType();

    private final MapRepository<MatcherType<?>> innerRepository = new MapRepository<MatcherType<?>>(MatcherType.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, MatcherType<?>> types = scanner.scanTypes(ParamMatcherType.class);
        innerRepository.registerAll(types);
    }

    @Override
    public MatcherType<?> getMatcherType(String matcherCode) {
        MatcherType<?> converter = innerRepository.getItem(matcherCode);
        if (converter == null) {
            converter = defaultDecoder;
        }
        return converter;
    }

    @Override
    public void register(String key, MatcherType<?> type) {
        innerRepository.register(key, type);
    }

    @Override
    public Map<String, MatcherType<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, MatcherType<?>> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
