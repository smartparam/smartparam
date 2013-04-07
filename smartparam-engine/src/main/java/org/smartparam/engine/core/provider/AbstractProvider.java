package org.smartparam.engine.core.provider;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.AnnotatedObjectsScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.engine.AbstractScanner;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public abstract class AbstractProvider<REGISTERED_OBJECT> extends AbstractScanner {

    public AbstractProvider() {
        super();
    }

    public AbstractProvider(boolean scanAnnotations, PackageList packagesToScan) {
        super(scanAnnotations, packagesToScan);
    }

    @PostConstruct
    public void scan() {
        if (isScanAnnotations()) {
            AnnotatedObjectsScanner<REGISTERED_OBJECT> scanner = new AnnotatedObjectsScanner<REGISTERED_OBJECT>();

            Map<String, REGISTERED_OBJECT> scannedTypes = scanner.getAnnotatedObjects(getPackagesToScan(), getAnnotationClass());
            for (Map.Entry<String, REGISTERED_OBJECT> typeEntry : scannedTypes.entrySet()) {
                handleRegistration(typeEntry.getKey(), typeEntry.getValue());
            }
        }
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract void handleRegistration(String objectCode, REGISTERED_OBJECT objectToRegister);
}
