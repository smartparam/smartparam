package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @param <REGISTERED_OBJECT>
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class PackagesAnnotationScanner<REGISTERED_OBJECT> {

    private PackageList packagesToScan;

    private Class<? extends Annotation> annotationType;

    public PackagesAnnotationScanner(Class<? extends Annotation> annotationType, PackageList packagesToScan) {
        this.annotationType = annotationType;
        this.packagesToScan = packagesToScan;
    }

    public Map<RepositoryObjectKey, REGISTERED_OBJECT> scan() {
        AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

        Map<RepositoryObjectKey, REGISTERED_OBJECT> objects = scanner.getAnnotatedObjects(annotationType, createPackagesForDefaults());
        Map<RepositoryObjectKey, REGISTERED_OBJECT> userObjects = scanner.getAnnotatedObjects(annotationType, packagesToScan);

        // override defaults
        objects.putAll(userObjects);

        return objects;
    }

    private PackageList createPackagesForDefaults() {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(packagesToScan.getDefaultPackage());
        return defaultPackages;
    }
}
