package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.scanner.AnnotatedObjectsScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.SmartParamConsts;
import org.smartparam.engine.core.engine.AbstractScanner;

/**
 *
 * @param <REGISTERED_OBJECT>
 * @author Adam Dubiel
 * @since 0.1.0
 */
public abstract class AbstractRepository<REGISTERED_OBJECT> extends AbstractScanner implements SmartParamConsts {

    private boolean alreadyScanned = false;

    public AbstractRepository() {
        super();
    }

    public AbstractRepository(boolean scanAnnotations, PackageList packagesToScan) {
        super(scanAnnotations, packagesToScan);
    }

    @PostConstruct
    public void scan() {
        if (!alreadyScanned && isScanAnnotations()) {
            alreadyScanned = true;
            AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

            Map<String, REGISTERED_OBJECT> objects = scanner.getAnnotatedObjects(createPackagesForDefaults(), getAnnotationClass());
            Map<String, REGISTERED_OBJECT> userObjects = scanner.getAnnotatedObjects(getPackagesToScan(), getAnnotationClass());

            // override defaults
            objects.putAll(userObjects);

            for (Map.Entry<String, REGISTERED_OBJECT> typeEntry : objects.entrySet()) {
                handleRegistration(typeEntry.getKey(), typeEntry.getValue());
            }
        }
    }

    private PackageList createPackagesForDefaults() {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(BASE_PACKAGE_PREFIX);
        return defaultPackages;
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract void handleRegistration(String objectCode, REGISTERED_OBJECT objectToRegister);
}
