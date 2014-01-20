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
package org.smartparam.engine.annotated.initialization;

import java.util.List;
import org.smartparam.engine.annotated.scanner.PackageMethodScanner;
import org.smartparam.engine.annotated.scanner.MethodScanner;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.annotated.repository.MethodScanningRepository;
import org.smartparam.engine.config.initialization.ComponentInitializer;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;

/**
 *
 * @author Adam Dubiel
 */
public class MethodScannerInitializer implements ComponentInitializer {

    private final PackageList packagesToScan;

    private final MethodScanner methodScanner;

    public MethodScannerInitializer(MethodScanner methodScanner, PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
        this.methodScanner = methodScanner;
    }

    public MethodScannerInitializer(PackageList packagesToScan) {
        this(new PackageMethodScanner(packagesToScan), packagesToScan);
    }

    @Override
    public void initializeObject(Object configObject, ComponentInitializerRunner runner) {
        MethodScanningRepository repository = (MethodScanningRepository) configObject;
        repository.scanMethods(methodScanner);
    }

    @Override
    public boolean acceptsObject(Object configObject) {
        return MethodScanningRepository.class.isAssignableFrom(configObject.getClass());
    }

    public PackageList getPackageList() {
        return packagesToScan;
    }

    public List<String> getDefaultPackages() {
        return packagesToScan.getDefaultPackages();
    }
}
