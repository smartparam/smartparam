package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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

    private PackageList createPackagesForDefaults(PackageList packagesToScan) {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(packagesToScan.getDefaultPackage());
        return defaultPackages;
    }
}
