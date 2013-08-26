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
package org.smartparam.engine.config.initialization;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.annotations.scanner.MethodScanner;
import org.smartparam.engine.core.repository.MethodScanningRepository;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class MethodScannerInitializerTest {

    private MethodScannerInitializer methodScannerInitializer;

    @BeforeMethod
    public void setUp() {
        methodScannerInitializer = new MethodScannerInitializer();
    }

    @Test
    public void shouldRunInitializationMethodOnProvidedObject() {
        // given
        MethodScanningRepository repository = mock(MethodScanningRepository.class);

        // when
        methodScannerInitializer.initializeObject(repository);

        // then
        verify(repository, times(1)).scanMethods(any(MethodScanner.class));
    }

    @Test
    public void shouldAcceptTypeScanningRepositoryImplementations() {
        // given
        MethodScanningRepository repository = mock(MethodScanningRepository.class);

        // when
        boolean accepted = methodScannerInitializer.acceptsObject(repository);

        // then
        assertThat(accepted).isTrue();
    }

    @Test
    public void shouldNotAcceptOtherObjectsThanTypeScanningRepositoryImplementations() {
        // given
        Object someObject = new Object();

        // when
        boolean accepted = methodScannerInitializer.acceptsObject(someObject);

        // then
        assertThat(accepted).isFalse();
    }
}