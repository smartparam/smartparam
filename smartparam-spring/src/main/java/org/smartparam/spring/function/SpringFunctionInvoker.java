package org.smartparam.spring.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
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
@SmartParamFunctionInvoker("spring")
public class SpringFunctionInvoker extends AbstractJavaFunctionInvoker implements ApplicationContextAware {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Kontekst aplikacji springa.
     */
    private ApplicationContext appContext = null;

    public Object invoke(Function function, Object... args) {
        SpringFunction springFunction = (SpringFunction) function;

        Object bean = appContext.getBean(springFunction.getBeanName());

        return invokeMethod(bean, springFunction.getMethod(), args);
    }

    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.appContext = ac;
    }
}
