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
import org.smartparam.engine.annotated.annotations.ParamMatcher;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherRepository;

/**
 * @see Matcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ScanningMatcherRepository implements MatcherRepository, TypeScanningRepository {

    private final MapRepository<Matcher> innerRepository = new MapRepository<Matcher>(Matcher.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner initializer) {
        Map<RepositoryObjectKey, Matcher> matchers = scanner.scanTypes(ParamMatcher.class);
        innerRepository.registerAll(matchers);
    }

    @Override
    public Matcher getMatcher(String code) {
        return innerRepository.getItem(code);
    }

    @Override
    public void register(String code, Matcher matcher) {
        innerRepository.register(code, matcher);
    }

    @Override
    public Map<String, Matcher> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, Matcher> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
