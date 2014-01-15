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
package org.smartparam.editor.annotated;

import java.util.Map;
import org.smartparam.editor.core.matcher.EmptyMatcherConverter;
import org.smartparam.editor.core.matcher.MatcherAwareConverter;
import org.smartparam.editor.core.matcher.MatcherConverterRepository;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.repository.TypeScanningRepository;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningMatcherConverterRepository implements MatcherConverterRepository, TypeScanningRepository {

    private final MatcherAwareConverter<?> defaultConverter = new EmptyMatcherConverter();

    private final MapRepository<MatcherAwareConverter<?>> innerRepository = new MapRepository<MatcherAwareConverter<?>>(MatcherAwareConverter.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, MatcherAwareConverter<?>> matcherConverters = scanner.scanTypes(ParamMatcherConverter.class);
        innerRepository.registerAll(matcherConverters);
    }

    @Override
    public MatcherAwareConverter<?> getConverter(String matcherCode) {
        MatcherAwareConverter<?> converter = innerRepository.getItem(matcherCode);
        if (converter == null) {
            converter = defaultConverter;
        }
        return converter;
    }

    @Override
    public void register(String key, MatcherAwareConverter<?> type) {
        innerRepository.register(key, type);
    }

    @Override
    public Map<String, MatcherAwareConverter<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, MatcherAwareConverter<?>> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
