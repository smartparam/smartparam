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
package org.smartparam.engine.core.context;

import java.math.BigDecimal;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;
import static org.smartparam.engine.test.assertions.Assertions.*;
import org.smartparam.engine.util.reflection.ReflectionSetterInvoker;

/**
 * @author Przemek Hertel
 */
public class DefaultContextTest {

    @Test
    public void shouldUseStringArrayAsLevelValuesWhenConstructingContext() {
        // given
        String[] levelValues = new String[]{"A", "B"};

        // when
        DefaultContext context = new DefaultContext((Object) levelValues);

        // then
        assertThat(context).hasLevelValues("A", "B");
    }

    @Test
    public void shouldUseObjectArrayAsLevelValuesWhenConstructingContext() {
        // given
        Object[] levelValues = new Object[] { 1, "A" };

        // when
        DefaultContext context = new DefaultContext("DUMMY", 1, levelValues);

        // then
        assertThat(context).hasLevelValues(1, "A");
    }

    @Test
    public void shouldUseStringAsKeyForNextValueWhenConstructingContext() {
        // given
        // when
        DefaultContext context = new DefaultContext("TEST1", 1, "TEST2", "2");

        // then
        assertThat(context).hasValue("TEST1", 1).hasValue("TEST2", "2");
    }

    @Test
    public void shouldUseReflectionSetterInvokerAsSetterInvokerIfFirstConstructorArgument() {
        // given
        ReflectionSetterInvoker setterInvoker = mock(ReflectionSetterInvoker.class);

        // when
        new DefaultContext(setterInvoker, BigDecimal.ONE);

        // then
        verify(setterInvoker, times(1)).invokeSetter(anyObject(), anyObject());
    }

    @Test
    public void shouldTreatReflectionSetterInvokerAsAnyOtherObjectIfNotFirstConstructorArgument() {
        // given
        ReflectionSetterInvoker invoker = new ReflectionSetterInvoker();

        // when
        DefaultContext context = new DefaultContext(BigDecimal.ONE, invoker);

        // then
        assertThat(context).hasValue("ReflectionSetterInvoker", invoker);
    }

    @Test
    public void shouldFindAndInvokeSetterOnContextObjectForPassedArgWhenConstructingContext() {
        // given
        ReflectionSetterInvoker setterInvoker = mock(ReflectionSetterInvoker.class);
        when(setterInvoker.invokeSetter(anyObject(), anyObject())).thenReturn(true);

        // when
        DefaultContext context = new DefaultContext(setterInvoker, BigDecimal.ONE);

        // then
        verify(setterInvoker, times(1)).invokeSetter(context, BigDecimal.ONE);
    }

    @Test
    public void shouldPutObjectUnderItsClassNameWhenConstructingContextAndNoOtherConditionMatch() {
        // given
        // when
        DefaultContext context = new DefaultContext(BigDecimal.ONE);

        // then
        assertThat(context).hasValue("BigDecimal", BigDecimal.ONE);
    }

    @Test
    public void shouldPutValueIntoTheContextUnderGivenName() {
        // given
        DefaultContext context = new DefaultContext();

        // when
        context.with("TEST", "A");

        // then
        assertThat(context).hasValue("TEST", "A");
    }

    @Test
    public void shouldThrowExceptionIfhereIsAlreadyValueRegisteredUnderTheSameKey() {
        // given
        DefaultContext context = new DefaultContext();
        context.with("TEST", "B");

        // when, cant use CatchException cos context uses static fields
        try {
            context.with("TEST", "A");
            fail();
        }
        catch(DuplicateContextItemException exception) {
            // then success
        }
    }

    @Test
    public void shouldAllowOnOverwritingValuesInContextWhenCallingMethodWithOverwriteFlag() {
        // given
        DefaultContext context = new DefaultContext();
        context.with("TEST", "B");

        // when
        context.with("TEST", "A", true);

        // then
        assertThat(context).hasValue("TEST", "A");
    }

    @Test
    public void shouldReturnObjectUnderClassSimpleNameAndMatchingTypeWhenAskingByClass() {
        // given
        DefaultContext context = new DefaultContext();
        context.with("BigDecimal", BigDecimal.ONE);

        // when
        BigDecimal value = context.get(BigDecimal.class);

        // then
        assertThat(value).isNotNull().isSameAs(BigDecimal.ONE);
    }

    @Test
    public void shouldReturnFirstObjectWithMatchingClassFromContextIfNoValueWithClassSimpleNameKey() {
        // given
        DefaultContext context = new DefaultContext();
        context.with("TEST", BigDecimal.ONE);

        // when
        BigDecimal value = context.get(BigDecimal.class);

        // then
        assertThat(value).isNotNull().isSameAs(BigDecimal.ONE);
    }

    @Test
    public void shouldReturnNullIfSearchByClassDidNotFindAnything() {
        // given
        DefaultContext context = new DefaultContext();

        // when
        BigDecimal value = context.get(BigDecimal.class);

        // then
        assertThat(value).isNull();
    }

    @Test
    public void shouldBeNullValuesProofWhenSearchingByClass() {
        // given
        DefaultContext context = new DefaultContext();
        context.with("TEST", null);

        // when
        BigDecimal value = context.get(BigDecimal.class);

        // then
        assertThat(value).isNull();
    }

    @Test
    public void shouldThrowExceptionWhenNoValueForKeyPassedToConstructor() {
        // given
        // when, cant use CatchException cos context uses static fields
        try {
            new DefaultContext("KEY_WITHOUT_VALUE");
            fail();
        }
        catch(InvalidContextArgumentsCountException exception) {
            // then success
        }
    }
}
