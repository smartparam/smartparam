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
package org.smartparam.engine.core.assembler;

import org.smartparam.engine.core.engine.MultiValue;
import org.testng.annotations.Test;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.smartparam.engine.test.builder.MultiValueBuilder.multiValue;

/**
 *
 * @author Adam Dubiel
 */
public class ConstructorAssemblyStrategyTest {

    @Test
    public void shouldAssembleObjectUsingConstructorArgumentsAnnotatedWithFromLevel() {
        // given
        ConstructorAssemblyStrategy constructorAssemblyStrategy = new ConstructorAssemblyStrategy();
        MultiValue row = multiValue().withNamedLevels("code", "value").withValues("testCode", 42).build();

        // when
        ClassAssembledWithFromLevelAnnotation result = constructorAssemblyStrategy.assemble(ClassAssembledWithFromLevelAnnotation.class, row);

        // then
        assertThat(result.code).isEqualTo("testCode");
        assertThat(result.value).isEqualTo(42);
    }

    private static class ClassAssembledWithFromLevelAnnotation {

        String code;

        int value;

        ClassAssembledWithFromLevelAnnotation(@FromParamLevel("value") int value, @FromParamLevel("code") String code) {
            this.code = code;
            this.value = value;
        }
    }
}