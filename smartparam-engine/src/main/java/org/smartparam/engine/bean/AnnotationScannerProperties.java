package org.smartparam.engine.bean;

import java.util.List;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotationScannerProperties {

    private boolean scanAnnotations = true;

    private PackageList packagesToScan = new PackageList();

    public void addPackagesToScan(List<String> packages) {
        for (String packageToScan : packages) {
            packagesToScan.addPackage(packageToScan);
        }
    }

    public PackageList getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public boolean isScanAnnotations() {
        return scanAnnotations;
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }
}
