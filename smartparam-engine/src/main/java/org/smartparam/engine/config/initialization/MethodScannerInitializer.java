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
package org.smartparam.engine.config.initialization;

import java.util.List;
import org.smartparam.engine.annotated.scanner.PackageMethodScanner;
import org.smartparam.engine.annotated.scanner.MethodScanner;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.config.ComponentInitializer;
import org.smartparam.engine.config.ComponentInitializerRunner;
import org.smartparam.engine.annotated.repository.MethodScanningRepository;

/**
 *
 * @author Adam Dubiel
 */
public class MethodScannerInitializer implements ComponentInitializer {

    private PackageList packagesToScan = new PackageList();

    private MethodScanner methodScanner;

    public MethodScannerInitializer() {
        methodScanner = new PackageMethodScanner(packagesToScan);
    }

    public MethodScannerInitializer(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
        methodScanner = new PackageMethodScanner(packagesToScan);
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

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }

    public String getDefaultPackage() {
        return packagesToScan.getDefaultPackage();
    }

    public void setDefaultPackage(String defaultPackage) {
        packagesToScan.setDefaultPackage(defaultPackage);
    }

    public void setMethodScanner(MethodScanner methodScanner) {
        this.methodScanner = methodScanner;
    }
}
