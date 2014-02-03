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
package org.smartparam.engine.functions.java;

import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class JavaMethodInvokerTest {

    @Test
    public void shouldInvokePublicJavaMethod() throws NoSuchMethodException {
        // given
        JavaMethodInvoker invoker = new JavaMethodInvoker();
        FunctionWrapper wrapper = new FunctionWrapper();

        // when
        invoker.invokeMethod(wrapper, FunctionWrapper.class.getDeclaredMethod("publicMethod"), false);

        // then
        assertThat(wrapper.publicInvoked).isTrue();
    }

    @Test
    public void shouldInvokeProtectedJavaMethod() throws NoSuchMethodException {
        // given
        JavaMethodInvoker invoker = new JavaMethodInvoker();
        FunctionWrapper wrapper = new FunctionWrapper();

        // when
        invoker.invokeMethod(wrapper, FunctionWrapper.class.getDeclaredMethod("protectedMethod"), true);

        // then
        assertThat(wrapper.protectedInvoked).isTrue();
    }

    @Test
    public void shouldWrapAnyExceptionComingFromFunctionInJavaFunctionInvocationException() throws NoSuchMethodException {
        // given
        JavaMethodInvoker invoker = new JavaMethodInvoker();
        FunctionWrapper wrapper = new FunctionWrapper();

        // when
        catchException(invoker).invokeMethod(wrapper, FunctionWrapper.class.getDeclaredMethod("throwingMethod"), true);

        // then
        assertThat(caughtException()).isInstanceOf(JavaFunctionInvocationException.class);
    }

    private class FunctionWrapper {

        boolean publicInvoked = false;

        boolean protectedInvoked = false;

        public void publicMethod() {
            publicInvoked = true;
        }

        void protectedMethod() {
            protectedInvoked = true;
        }

        void throwingMethod() {
            throw new IllegalStateException();
        }
    }
}
