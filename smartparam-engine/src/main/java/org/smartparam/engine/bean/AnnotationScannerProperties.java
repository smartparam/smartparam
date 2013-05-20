package org.smartparam.engine.bean;

import java.util.List;

/**
 * Object with instructions on which packages to scan for annotations, if
 * scanning is enabled.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotationScannerProperties {

    /**
     * Is scanning enabled.
     */
    private boolean scanAnnotations = true;

    /**
     * List of packages to scan.
     */
    private PackageList packagesToScan = new PackageList();

    /**
     * Add list of packages names to scan.
     *
     * @param packages packages names, not null
     */
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
