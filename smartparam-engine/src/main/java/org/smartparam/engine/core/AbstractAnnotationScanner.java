package org.smartparam.engine.core;

import java.util.List;
import org.smartparam.engine.bean.AnnotationScannerProperties;
import org.smartparam.engine.bean.PackageList;

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

    public boolean isScanAnnotations() {
        return properties.isScanAnnotations();
    }

    @Override
    public void setScanAnnotations(boolean scanAnnotations) {
        properties.setScanAnnotations(scanAnnotations);
    }

    public PackageList getPackagesToScan() {
        return properties.getPackagesToScan();
    }

    @Override
    public void setPackagesToScan(List<String> packagesToScan) {
        properties.addPackagesToScan(packagesToScan);
    }

    @Override
    public AnnotationScannerProperties getScannerProperties() {
        return properties;
    }

    @Override
    public void setScannerProperties(AnnotationScannerProperties properties) {
        this.properties = properties;
    }
}
