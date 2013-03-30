package org.smartparam.engine.core.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.AnnotatedObjectsScanner;
import org.smartparam.engine.annotations.handler.SmartParamTypeAnnotationHandler;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.type.AbstractType;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedTypeProvider extends SmartTypeProvider {

    private PackageList packagesToScan = new PackageList();

    @PostConstruct
    public void scan() {
        SmartParamTypeAnnotationHandler handler = new SmartParamTypeAnnotationHandler();
        AnnotatedObjectsScanner scanner = new AnnotatedObjectsScanner();

        Map<String, AbstractType> scannedTypes = scanner.getAnnotatedObjects(packagesToScan, handler, AbstractType.class);
        for (Entry<String, AbstractType> typeEntry : scannedTypes.entrySet()) {
            registerType(typeEntry.getKey(), typeEntry.getValue());
        }
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }
}
