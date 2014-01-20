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
import org.smartparam.engine.annotated.annotations.ParamMatcherDecoder;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.matcher.MatcherAwareDecoder;
import org.smartparam.engine.core.matcher.MatcherDecoderRepository;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.matchers.decoder.EmptyMatcherDecoder;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningMatcherDecoderRepository implements MatcherDecoderRepository, TypeScanningRepository {

    private final MatcherAwareDecoder<?> defaultDecoder = new EmptyMatcherDecoder();

    private final MapRepository<MatcherAwareDecoder<?>> innerRepository = new MapRepository<MatcherAwareDecoder<?>>(MatcherAwareDecoder.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, MatcherAwareDecoder<?>> matcherConverters = scanner.scanTypes(ParamMatcherDecoder.class);
        innerRepository.registerAll(matcherConverters);
    }

    @Override
    public MatcherAwareDecoder<?> getDecoder(String matcherCode) {
        MatcherAwareDecoder<?> converter = innerRepository.getItem(matcherCode);
        if (converter == null) {
            converter = defaultDecoder;
        }
        return converter;
    }

    @Override
    public void register(String key, MatcherAwareDecoder<?> type) {
        innerRepository.register(key, type);
    }

    @Override
    public Map<String, MatcherAwareDecoder<?>> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, MatcherAwareDecoder<?>> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
