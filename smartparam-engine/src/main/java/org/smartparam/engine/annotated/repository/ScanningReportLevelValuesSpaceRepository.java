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
import org.smartparam.engine.annotated.annotations.ParamReportLevelSpaceFactory;
import org.smartparam.engine.annotated.scanner.TypeScanner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.report.space.ReportLevelValuesSpace;
import org.smartparam.engine.report.space.ReportLevelValuesSpaceFactory;
import org.smartparam.engine.report.space.ReportLevelValuesSpaceRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ScanningReportLevelValuesSpaceRepository implements ReportLevelValuesSpaceRepository, TypeScanningRepository {

    private final MapRepository<ReportLevelValuesSpaceFactory> innerRepository = new MapRepository<ReportLevelValuesSpaceFactory>(ReportLevelValuesSpaceFactory.class);

    @Override
    public void scanAnnotations(TypeScanner scanner, ComponentInitializerRunner componentInitializerRunner) {
        Map<RepositoryObjectKey, ReportLevelValuesSpaceFactory> factories = scanner.scanTypes(ParamReportLevelSpaceFactory.class);
        innerRepository.registerAll(factories);
    }

    @Override
    public ReportLevelValuesSpaceFactory getSpaceFactory(String matcherCode) {
        return innerRepository.getItem(matcherCode);
    }

    @Override
    public void register(String key, ReportLevelValuesSpaceFactory type) {
        innerRepository.register(key, type);
    }

    @Override
    public Map<String, ReportLevelValuesSpaceFactory> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, ReportLevelValuesSpaceFactory> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
