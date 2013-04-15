package org.smartparam.engine.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageList implements Iterable<String> {

    private List<String> packages = null;

    private void createIfNull() {
        if (packages == null) {
            packages = new LinkedList<String>();
        }
    }

    public void addPackage(String packageName) {
        createIfNull();
        packages.add(packageName);
    }

    public List<String> getPackages() {
        createIfNull();
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public Iterator<String> iterator() {
        createIfNull();
        return packages.iterator();
    }
}
