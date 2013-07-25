/*
 * Copyright 2013 the original author or authors.
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
package org.smartparam.repository.staticFactory;

import org.smartparam.engine.annotations.scanner.PackageTypeScanner;
import org.smartparam.engine.bean.PackageList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.fest.assertions.api.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;
import static org.smartparam.engine.test.builder.PackageListTestBuilder.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StaticParamRepositoryIntegrationTest {

    private StaticParamRepository staticParamRepository;

    @BeforeMethod
    public void setUp() {
        staticParamRepository = new StaticParamRepository();
    }

    @Test
    public void shouldReturnParameterCreatedByFactoryDiscoveredUsingAnnotation() {
        // given
        PackageList packageList = packageList().withPackage("org.smartparam.repository.staticFactory.test").build();

        // when
        staticParamRepository.scanAnnotations(new PackageTypeScanner(packageList));

        // then
        assertThat(staticParamRepository.load("testParameter")).isNotNull();
    }

    @Test
    public void shouldReturnNullWhenNoParameterFound() {
        // given
        PackageList packageList = packageList().withPackage("org.smartparam.repository.java.test").build();

        // when
        staticParamRepository.scanAnnotations(new PackageTypeScanner(packageList));

        // then
        assertThat(staticParamRepository.load("FAKE PARAMETER")).isNull();
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenTryingToLoadUncacheableParameter() {
        // given

        // when
        catchException(staticParamRepository).findEntries(null, null);

        // then
        assertThat(caughtException()).isInstanceOf(UnsupportedOperationException.class);
    }
}