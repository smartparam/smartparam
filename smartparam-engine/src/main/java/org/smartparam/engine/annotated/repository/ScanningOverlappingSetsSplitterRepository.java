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
import org.smartparam.engine.annotated.annotations.ParamOverlappingSetsSplitter;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.report.OverlappingSetsSplitter;
import org.smartparam.engine.report.OverlappingSetsSplitterRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningOverlappingSetsSplitterRepository implements OverlappingSetsSplitterRepository, TypeScanningRepository {

    private final MapRepository<OverlappingSetsSplitter<?>> innerRepository = new MapRepository<OverlappingSetsSplitter<?>>(OverlappingSetsSplitter.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, OverlappingSetsSplitter<?>> splitters = scanner.scanTypes(ParamOverlappingSetsSplitter.class);
        innerRepository.registerAll(splitters);
    }

    @Override
    public OverlappingSetsSplitter<?> getSplitter(String matcherCode) {
        return innerRepository.getItem(matcherCode);
    }

    @Override
    public void register(String key, OverlappingSetsSplitter<?> type) {
        innerRepository.register(key, type);
    }

    @Override
    public Map<String, OverlappingSetsSplitter<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, OverlappingSetsSplitter<?>> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
