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
package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel
 */
public class PackageTypeScanner implements TypeScanner {

    private PackageList packagesToScan;

    public PackageTypeScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public <REGISTERED_OBJECT> Map<RepositoryObjectKey, REGISTERED_OBJECT> scanTypes(Class<? extends Annotation> annotationType) {
        AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

        Map<RepositoryObjectKey, REGISTERED_OBJECT> objects = scanner.getAnnotatedObjects(annotationType, createPackagesForDefaults(packagesToScan));
        Map<RepositoryObjectKey, REGISTERED_OBJECT> userObjects = scanner.getAnnotatedObjects(annotationType, packagesToScan);

        // override defaults
        objects.putAll(userObjects);

        return objects;
    }

    @Override
    public <REGISTERED_OBJECT> List<REGISTERED_OBJECT> scanTypesWithoutName(Class<? extends Annotation> annotationType) {
        AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();
        return scanner.getAnnotatedObjectsWithoutName(annotationType, packagesToScan);
    }

    private PackageList createPackagesForDefaults(PackageList packagesToScan) {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(packagesToScan.getDefaultPackage());
        return defaultPackages;
    }
}
