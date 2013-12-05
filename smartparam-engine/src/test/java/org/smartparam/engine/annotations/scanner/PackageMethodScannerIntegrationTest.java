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

import org.smartparam.engine.annotated.scanner.PackageMethodScanner;
import java.lang.reflect.Method;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.annotated.annotations.JavaPlugin;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotation;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotationWithoutValue;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.annotated.PackageListTestBuilder.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel
 */
public class PackageMethodScannerIntegrationTest {

    private static final String TEST_PACKAGE = "org.smartparam.engine.test.scan.plugins";

    private PackageList packageList;

    @BeforeMethod
    public void setUp() {
        packageList = packageList().withPackage(TEST_PACKAGE).build();
    }

    @Test
    public void shouldScanMethodsAndReturnAllAnnotatedWithType() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        Map<String, Method> methods = scanner.scanMethods(JavaPlugin.class);

        // then
        assertThat(methods).hasSize(1).containsKey("javaPlugin");
    }

    @Test
    public void shouldFailWhenRegisteringTwoPluginsWithSameName() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        catchException(scanner).scanMethods(DummyPluginAnnotation.class);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).isNotNull().hasErrorCode(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE);
    }

    @Test
    public void shouldFailWhenPluginAnnotationHasNoValueMethod() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        catchException(scanner).scanMethods(DummyPluginAnnotationWithoutValue.class);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).isNotNull().hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }
}
