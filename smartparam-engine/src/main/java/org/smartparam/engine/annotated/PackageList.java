/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.annotated;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates list of packages to scan, can be iterated. Order is retained.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageList implements Iterable<String> {

    private static final String DEFAULT_PACKAGE = "org.smartparam.engine";

    private String defaultPackage = DEFAULT_PACKAGE;

    /**
     * Internal representation of package list.
     */
    private final List<String> packages = new LinkedList<String>();

    public PackageList() {
    }

    public PackageList(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public void addPackage(String packageName) {
        packages.add(packageName);
    }

    public void addAllPackages(String... packages) {
        this.packages.addAll(Arrays.asList(packages));
    }

    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    /**
     * Return ordered list of all packages.
     *
     * @return package names
     */
    public List<String> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    /**
     * Replaces currently defined packages with those on provided list.
     *
     * @param packages list of package, content is copied
     */
    public void setPackages(List<String> packages) {
        this.packages.clear();
        if (packages != null) {
            this.packages.addAll(packages);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return packages.iterator();
    }
}