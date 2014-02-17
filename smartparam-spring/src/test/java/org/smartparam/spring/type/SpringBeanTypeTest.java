/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.spring.type;

import org.smartparam.spring.context.TestBean;
import org.smartparam.spring.context.TestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
@ContextConfiguration(classes = TestContext.class)
public class SpringBeanTypeTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void shouldResolveBeanFromContext() {
        // given
        SpringBeanType type = new SpringBeanType(context);

        // when
        TestBean bean = (TestBean) type.decode("testBean").getValue();

        // then
        assertThat(bean).isNotNull();
    }

    @Test
    public void shouldThrowBeanNotFoundExceptionWhenNoSuchBeanExistsInContext() {
        // given
        SpringBeanType type = new SpringBeanType(context);

        // when
        catchException(type).decode("unknownBean");

        // then
        assertThat(caughtException()).isInstanceOf(BeanNotFoundException.class);
    }
}
