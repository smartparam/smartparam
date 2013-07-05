package org.smartparam.engine.config;

import java.util.List;
import org.smartparam.engine.annotations.scanner.PackageMethodScanner;
import org.smartparam.engine.annotations.scanner.MethodScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.repository.MethodScanningRepository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class MethodScannerInitializer implements ComponentInitializer {

    private PackageList packagesToScan = new PackageList();

    private MethodScanner methodScanner = new PackageMethodScanner(packagesToScan);

    @Override
    public void initializeObject(ComponentInitializerRunner initializerRunner, Object configObject) {
        MethodScanningRepository repository = (MethodScanningRepository) configObject;
        repository.scanMethods(methodScanner);
    }

    @Override
    public boolean acceptsObject(Object configObject) {
        return MethodScanningRepository.class.isAssignableFrom(configObject.getClass());
    }

    public PackageList getPackageList() {
        return packagesToScan;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }

    public String getDefaultPackage() {
        return packagesToScan.getDefaultPackage();
    }

    public void setDefaultPackage(String defaultPackage) {
        packagesToScan.setDefaultPackage(defaultPackage);
    }

    public void setMethodScanner(MethodScanner methodScanner) {
        this.methodScanner = methodScanner;
    }
}
