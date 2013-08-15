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
package org.smartparam.engine.config;

import java.util.Arrays;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel
 */
public class BasicComponentInitializerRunnerTest {

    private BasicComponentInitializerRunner basicComponentInitializerRunner;

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void shouldRunAllContextInitializersAcceptingObject() {
        // given
        Object initialiedObject = new Object();
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(initialiedObject)).thenReturn(true);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializers(initialiedObject);

        // then
        verify(initializer, times(1)).initializeObject(initialiedObject);
    }

    @Test
    public void shouldNotRunContextInitializerThatDoesNotAcceptObject() {
        // given
        Object initialiedObject = new Object();
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(initialiedObject)).thenReturn(false);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializers(initialiedObject);

        // then
        verify(initializer, never()).initializeObject(initialiedObject);
    }

    @Test
    public void shouldIterateThroughGivenListAndTryToInitializeObjects() {
        // given
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(anyObject())).thenReturn(true);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializersOnList(Arrays.asList(new Object(), new Object()));

        // then
        verify(initializer, times(2)).initializeObject(anyObject());
    }
}