package org.smartparam.engine.test.builder;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.bean.PackageList;

/**
 *
 * @author Adam Dubiel
 */
public class PackageListTestBuilder {

    private String defaultPackage;

    private List<String> packages = new ArrayList<String>();

    private PackageListTestBuilder() {
    }

    public static PackageListTestBuilder packageList() {
        return new PackageListTestBuilder();
    }

    public PackageList build() {
        PackageList packageList = new PackageList(defaultPackage);
        packageList.setPackages(packages);
        return packageList;
    }

    public PackageListTestBuilder withPackage(String packageName) {
        packages.add(packageName);
        return this;
    }

    public PackageListTestBuilder withDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
        return this;
    }
}
