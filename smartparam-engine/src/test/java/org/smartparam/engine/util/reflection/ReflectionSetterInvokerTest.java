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

import java.lang.reflect.Method;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class ReflectionSetterInvokerTest {

    private ReflectionSetterInvoker reflectionSetterInvoker;

    @BeforeMethod
    public void setUp() {
        reflectionSetterInvoker = new ReflectionSetterInvoker();
    }

    @Test
    public void shouldFindSingleArgumentVoidMethodAndRecognizeItAsASetterNevermindingNamingConvention() {
        // given
        // when
        Method method = reflectionSetterInvoker.findSetter(ReflectionSetterInvokerTestObject.class, "TEST");

        // then
        assertThat(method.getName()).isEqualTo("inconventionalSetter");
    }

    @Test
    public void shouldInvokePubliclyAvailableSetter() {
        // given
        ReflectionSetterInvokerTestObject testObject = new ReflectionSetterInvokerTestObject();

        // when
        boolean invoked = reflectionSetterInvoker.invokeSetter(testObject, "TEST");

        // then
        assertThat(invoked).isTrue();
        assertThat(testObject.inconventionalSetterArg).isEqualTo("TEST");
    }

    @Test
    public void shouldInvokePrivateSetter() {
        // given
        ReflectionSetterInvokerTestObject testObject = new ReflectionSetterInvokerTestObject();

        // when
        boolean invoked = reflectionSetterInvoker.invokeSetter(testObject, Integer.valueOf(1));

        // then
        assertThat(invoked).isTrue();
        assertThat(testObject.privateSetterArg).isEqualTo(1);
    }

    @Test
    public void shouldReturnFalseWhenSetterNotFound() {
        // given
        ReflectionSetterInvokerTestObject testObject = new ReflectionSetterInvokerTestObject();

        // when
        boolean invoked = reflectionSetterInvoker.invokeSetter(testObject, Long.valueOf(1));

        // then
        assertThat(invoked).isFalse();
    }

    private static class ReflectionSetterInvokerTestObject {

        String inconventionalSetterArg;

        Integer privateSetterArg;

        public void inconventionalSetter(String arg) {
            this.inconventionalSetterArg = arg;
        }

        private void privateSetter(Integer arg) {
            this.privateSetterArg = arg;
        }
    }
}