package org.smartparam.spring.function;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.core.repository.AbstractJavaFunctionRepository;
import org.smartparam.engine.model.function.Function;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
@SmartParamFunctionRepository("spring")
public class SpringFunctionRepository extends AbstractJavaFunctionRepository {

    private AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    /**
     * In order to resolve bean name from class using beanNameGenerator we
     * need a registry - serves no other purpose.
     */
    private SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

    @Override
    protected Class<? extends Annotation> annotationClass() {
        return SmartParamSpringPlugin.class;
    }

    @Override
    protected Function createFunction(String functionName, Method method) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(method.getDeclaringClass());
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);

        SpringFunction springFunction = new SpringFunction(functionName, "spring", beanName, method);

        return springFunction;
    }
}
