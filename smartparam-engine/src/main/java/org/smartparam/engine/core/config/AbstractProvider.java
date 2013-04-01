package org.smartparam.engine.core.config;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.AnnotatedObjectsScanner;
import org.smartparam.engine.bean.PackageList;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public abstract class AbstractProvider<REGISTERED_OBJECT> {

    private boolean scanAnnotations = true;

    private PackageList packagesToScan = new PackageList();

    @PostConstruct
    public void scan() {
        if (scanAnnotations) {
            AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

            Map<String, REGISTERED_OBJECT> scannedTypes = scanner.getAnnotatedObjects(packagesToScan, getAnnotationClass());
            for (Map.Entry<String, REGISTERED_OBJECT> typeEntry : scannedTypes.entrySet()) {
                handleRegistration(typeEntry.getKey(), typeEntry.getValue());
            }
        }
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract void handleRegistration(String objectCode, REGISTERED_OBJECT objectToRegister);

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }
}
