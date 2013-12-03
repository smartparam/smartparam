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
package org.smartparam.engine.annotated.scanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.annotated.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel
 */
public class PackageTypeScanner implements TypeScanner {

    private final PackageList packagesToScan;

    public PackageTypeScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public <T> Map<RepositoryObjectKey, T> scanTypes(Class<? extends Annotation> annotationType) {
        AnnotatedObjectsScanner<T> scanner = new AnnotatedObjectsScanner<T>();

        Map<RepositoryObjectKey, T> objects = scanner.getAnnotatedObjects(annotationType, createPackagesForDefaults(packagesToScan));
        Map<RepositoryObjectKey, T> userObjects = scanner.getAnnotatedObjects(annotationType, packagesToScan);

        // override defaults
        objects.putAll(userObjects);

        return objects;
    }

    @Override
    public <T> List<T> scanTypesWithoutName(Class<? extends Annotation> annotationType) {
        AnnotatedObjectsScanner<T> scanner = new AnnotatedObjectsScanner<T>();
        return scanner.getAnnotatedObjectsWithoutName(annotationType, packagesToScan);
    }

    private PackageList createPackagesForDefaults(PackageList packagesToScan) {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(packagesToScan.getDefaultPackage());
        return defaultPackages;
    }
}
