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
package org.smartparam.engine.bean;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageListTest {

    private PackageList packageList = null;

    @BeforeMethod
    public void setUp() {
        packageList = new PackageList();
    }

    @Test
    public void shouldReturnEmptyListWhenNoPackagesAdded() {
        // given

        // when

        // then
        assertThat(packageList.getPackages()).isNotNull();
    }
}
