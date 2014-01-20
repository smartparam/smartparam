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

import java.util.*;

/**
 * Encapsulates list of packages to scan, can be iterated. Order is retained.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageList implements Iterable<String> {

    private static final String[] DEFAULT_PACKAGES = {
        "org.smartparam.engine.matchers",
        "org.smartparam.engine.types",
        "org.smartparam.engine.functions",
        "org.smartparam.engine.annotated.repository"};

    private final List<String> defaultPackages = new ArrayList<String>();

    /**
     * Internal representation of package list.
     */
    private final List<String> packages = new ArrayList<String>();

    public PackageList() {
        this.defaultPackages.addAll(Arrays.asList(DEFAULT_PACKAGES));
    }

    public PackageList(String... defaultPackages) {
        this.defaultPackages.addAll(Arrays.asList(defaultPackages));
    }

    public PackageList(List<String> defaultPackages, List<String> packages) {
        this.defaultPackages.addAll(defaultPackages);
        this.packages.addAll(packages);
    }

    public PackageList(List<String> packages) {
        this.defaultPackages.addAll(Arrays.asList(DEFAULT_PACKAGES));
        this.packages.addAll(packages);
    }

    public void add(String packageName) {
        packages.add(packageName);
    }

    public void addAll(String... packages) {
        this.packages.addAll(Arrays.asList(packages));
    }

    public void addAll(Collection<String> packages) {
        this.packages.addAll(packages);
    }

    public List<String> getDefaultPackages() {
        return Collections.unmodifiableList(defaultPackages);
    }

    public void setDefaultPackages(String... defaultPackages) {
        this.defaultPackages.clear();
        this.defaultPackages.addAll(Arrays.asList(defaultPackages));
    }

    /**
     * Return ordered list of all packages.
     *
     * @return package names
     */
    public List<String> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    @Override
    public Iterator<String> iterator() {
        return packages.iterator();
    }
}
