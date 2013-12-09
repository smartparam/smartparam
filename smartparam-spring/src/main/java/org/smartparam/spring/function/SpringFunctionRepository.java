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
package org.smartparam.spring.function;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.smartparam.engine.annotated.annotations.ParamFunctionRepository;
import org.smartparam.engine.annotated.repository.AbstractScanningJavaFunctionRepository;
import org.smartparam.engine.core.function.Function;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 *
 * @author Adam Dubiel
 */
@ParamFunctionRepository(SpringFunctionRepository.FUNCTION_TYPE)
public class SpringFunctionRepository extends AbstractScanningJavaFunctionRepository {

    public static final String FUNCTION_TYPE = "spring";

    private final AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    /**
     * In order to resolve bean name from class using beanNameGenerator we
     * need a registry - serves no other purpose.
     */
    private final SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

    @Override
    protected Class<? extends Annotation> annotationClass() {
        return SpringPlugin.class;
    }

    @Override
    protected Function createFunction(String functionName, Method method) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(method.getDeclaringClass());
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);

        SpringFunction springFunction = new SpringFunction(functionName, FUNCTION_TYPE, beanName, method);

        return springFunction;
    }

    @Override
    protected Class<? extends Function> functionClass() {
        return SpringFunction.class;
    }
}
