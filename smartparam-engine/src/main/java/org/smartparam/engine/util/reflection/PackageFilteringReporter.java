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
package org.smartparam.engine.util.reflection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class PackageFilteringReporter {

    private ClassLoader classLoader;

    private Set<String> cachedWantedPackages = new HashSet<String>();

    private Set<String> wantedPackagesPrefixes;

    public PackageFilteringReporter(ClassLoader classLoader, String[] wantedPackages) {
        this.classLoader = classLoader;
        this.wantedPackagesPrefixes = new HashSet<String>(Arrays.asList(wantedPackages));
    }

    protected Class<?> loadClass(String className) {
        return ReflectionsHelper.loadClass(classLoader, className);
    }

    protected boolean isWanted(String packageName) {
        if (cachedWantedPackages.contains(packageName)) {
            return true;
        }
        for (String packagePrefix : wantedPackagesPrefixes) {
            if (packageName.startsWith(packagePrefix)) {
                cachedWantedPackages.add(packageName);
                return true;
            }
        }
        return false;
    }
}
