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

import org.smartparam.engine.core.type.SimpleType;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 *
 * @author Adam Dubiel
 */
public class SpringBeanType extends SimpleType<Object> {

    public static final String BEAN_TYPE = "springBean";

    private final ApplicationContext context;

    private final AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    /**
     * In order to resolve bean name from class using beanNameGenerator we
     * need a registry - serves no other purpose.
     */
    private final SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

    public SpringBeanType(ApplicationContext context) {
        super(Object.class);
        this.context = context;
    }

    @Override
    protected String encodeValue(Object value) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(value.getClass());
        return beanNameGenerator.generateBeanName(beanDefinition, registry);
    }

    @Override
    protected Object decodeValue(String beanName) {
        if (context.containsBean(beanName)) {
            return context.getBean(beanName);
        }
        throw new BeanNotFoundException(beanName);
    }

}
