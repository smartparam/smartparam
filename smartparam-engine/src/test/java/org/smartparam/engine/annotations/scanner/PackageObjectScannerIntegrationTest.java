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
package org.smartparam.engine.annotations.scanner;

import org.smartparam.engine.annotated.scanner.AnnotatedObjectsScanner;
import java.util.Map;
import org.smartparam.engine.annotated.annotations.ParamMatcher;
import org.smartparam.engine.annotated.annotations.ParamType;
import org.smartparam.engine.annotated.annotations.ParamFunctionInvoker;
import org.smartparam.engine.annotated.annotations.ParamFunctionRepository;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparamtestscan.annotation.DummyAnnotationWithoutInstances;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.PackageListTestBuilder.*;
import static com.googlecode.catchexception.CatchException.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparamtestscan.annotation.DummyAnnotationWithoutOrder;
import org.smartparamtestscan.annotation.DummyAnnotationWithoutValue;
import org.smartparamtestscan.annotation.DummyAnnotationWithoutValues;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageObjectScannerIntegrationTest {

    private static final String TEST_PACKAGE = "org.smartparamtestscan";

    private PackageList packageList;

    @BeforeMethod
    public void setUp() {
        packageList = packageList().withPackage(TEST_PACKAGE).build();
    }

    @Test
    public void shouldFindSingleBeanWhenScanningForParamMatchers() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamMatcher.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsRepositoryKey("dummyMatcher").hasSize(1);
    }

    @Test
    public void shouldCreateTwoInstancesOfSingleBeanClass() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamType.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsObjectsThatAreNotSame("typeInstanceOne", "typeInstanceTwo").hasSize(2);
    }

    @Test
    public void shouldRegisterSingleBeanUnderMultipleNames() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamFunctionInvoker.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsObjectsThatAreSame("nameOne", "nameTwo").hasSize(2);
    }

    @Test
    public void shouldReturnBeansInOrder() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamFunctionRepository.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsRepositoryKeys("primaryRepository", "secondaryRepsitory");
    }

    @Test
    public void shouldFailToScanAnnotationWithoutInstancesMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutInstances.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutValueMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutValue.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutValuesMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutValues.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutOrderMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutOrder.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }
}
