package org.smartparam.engine.core.config;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.AnnotatedObjectsScanner;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.index.Matcher;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotationMatcherProvider extends SmartMatcherProvider {

    private PackageList packagesToScan = new PackageList();

    @PostConstruct
    public void scan() {
        AnnotatedObjectsScanner<Matcher> scanner = new AnnotatedObjectsScanner<Matcher>();

        Map<String, Matcher> scannedTypes = scanner.getAnnotatedObjects(packagesToScan, SmartParamMatcher.class);
        for (Map.Entry<String, Matcher> typeEntry : scannedTypes.entrySet()) {
            registerMatcher(typeEntry.getKey(), typeEntry.getValue());
        }
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }

}
