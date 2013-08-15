package org.smartparam.spring.function;

import java.lang.reflect.Method;
import org.smartparam.engine.model.function.JavaFunction;

/**
 *
 * @author Adam Dubiel
 */
public class SpringFunction extends JavaFunction {

    private String beanName;

    public SpringFunction(String name, String type, String beanName, Method method) {
        super(name, type, method);
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
