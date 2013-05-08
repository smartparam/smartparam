package org.smartparam.engine.core;

import java.util.List;
import org.smartparam.engine.bean.AnnotationScannerProperties;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public abstract class AbstractAnnotationScanner implements AnnotationScanner {

    private AnnotationScannerProperties properties;

    public AbstractAnnotationScanner() {
        properties = new AnnotationScannerProperties();
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        properties.setScanAnnotations(scanAnnotations);
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        properties.addPackagesToScan(packagesToScan);
    }

    public AnnotationScannerProperties getScannerProperties() {
        return properties;
    }

    public void setScannerProperties(AnnotationScannerProperties properties) {
        this.properties = properties;
    }
}
