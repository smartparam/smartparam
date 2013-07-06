package org.smartparam.engine.config;

import java.util.List;
import org.smartparam.engine.annotations.scanner.PackageTypeScanner;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.TypeScanner;
import org.smartparam.engine.core.repository.TypeScanningRepository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class TypeScannerInitializer implements ComponentInitializer {

    private PackageList packagesToScan = new PackageList();

    private TypeScanner typeScanner = new PackageTypeScanner(packagesToScan);

    @Override
    public void initializeObject(Object configObject) {
        TypeScanningRepository repository = (TypeScanningRepository) configObject;
        repository.scanAnnotations(typeScanner);
    }

    @Override
    public boolean acceptsObject(Object configObject) {
        return TypeScanningRepository.class.isAssignableFrom(configObject.getClass());
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

    public void setTypeScanner(TypeScanner typeScanner) {
        this.typeScanner = typeScanner;
    }
}
