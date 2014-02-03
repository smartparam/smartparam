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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class PackageListTestBuilder {

    private String defaultPackage;

    private final List<String> packages = new ArrayList<String>();

    private PackageListTestBuilder() {
    }

    public static PackageListTestBuilder packageList() {
        return new PackageListTestBuilder();
    }

    public PackageList build() {
        PackageList packageList = new PackageList(Arrays.asList(defaultPackage), packages);
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
