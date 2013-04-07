package org.smartparam.engine.core.engine;

import java.util.List;
import org.smartparam.engine.bean.PackageList;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public abstract class AbstractScanner {

    private boolean scanAnnotations = true;

    private PackageList packagesToScan;

    public AbstractScanner() {
        packagesToScan = new PackageList();
    }

    public AbstractScanner(boolean scanAnnotations, PackageList packagesToScan) {
        this.scanAnnotations = scanAnnotations;
        this.packagesToScan = packagesToScan;
    }

    protected boolean isScanAnnotations() {
        return scanAnnotations;
    }

    protected PackageList getPackagesToScan() {
        return packagesToScan;
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }
}
