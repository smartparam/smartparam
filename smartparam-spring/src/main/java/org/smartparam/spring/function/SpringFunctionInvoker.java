package org.smartparam.spring.function;

import org.smartparam.engine.annotations.ParamFunctionInvoker;
import org.smartparam.engine.core.invoker.AbstractJavaFunctionInvoker;
import org.smartparam.engine.model.function.Function;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@ParamFunctionInvoker("spring")
public class SpringFunctionInvoker extends AbstractJavaFunctionInvoker implements ApplicationContextAware {

    private ApplicationContext appContext = null;

    @Override
    public Object invoke(Function function, Object... args) {
        SpringFunction springFunction = (SpringFunction) function;

        Object bean = appContext.getBean(springFunction.getBeanName());

        return invokeMethod(bean, springFunction.getMethod(), args);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.appContext = ac;
    }
}
