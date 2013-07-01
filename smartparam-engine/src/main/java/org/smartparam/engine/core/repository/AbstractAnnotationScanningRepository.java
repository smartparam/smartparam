package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.annotations.scanner.AnnotatedObjectsScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.SmartParamConsts;
import org.smartparam.engine.core.AbstractAnnotationScanner;
import org.smartparam.engine.core.AnnotationScanner;

/**
 *
 * @param <REGISTERED_OBJECT>
 * @author Adam Dubiel
 * @since 0.1.0
 */
public abstract class AbstractAnnotationScanningRepository<REGISTERED_OBJECT> extends AbstractAnnotationScanner implements AnnotationScanningRepository, SmartParamConsts {

    private boolean alreadyScanned = false;

    @PostConstruct
    @Override
    public void scan() {
        if (!alreadyScanned && isScanAnnotations()) {
            alreadyScanned = true;

            AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

            Map<RepositoryObjectKey, REGISTERED_OBJECT> objects = scanner.getAnnotatedObjects(getAnnotationClass(), createPackagesForDefaults());
            Map<RepositoryObjectKey, REGISTERED_OBJECT> userObjects = scanner.getAnnotatedObjects(getAnnotationClass(), getPackagesToScan());

            // override defaults
            objects.putAll(userObjects);

            for (Map.Entry<RepositoryObjectKey, REGISTERED_OBJECT> typeEntry : objects.entrySet()) {
                register(typeEntry.getKey(), typeEntry.getValue());
            }
        }
    }

    private PackageList createPackagesForDefaults() {
        PackageList defaultPackages = new PackageList();
        defaultPackages.addPackage(getPackagesToScan().getDefaultPackage());
        return defaultPackages;
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    private void register(RepositoryObjectKey key, REGISTERED_OBJECT objectToRegister) {
        if (objectToRegister instanceof AnnotationScanner) {
            ((AnnotationScanner) objectToRegister).setScannerProperties(getScannerProperties());
        }
        handleRegistration(key, objectToRegister);
    }

    protected abstract void handleRegistration(RepositoryObjectKey key, REGISTERED_OBJECT objectToRegister);
}
