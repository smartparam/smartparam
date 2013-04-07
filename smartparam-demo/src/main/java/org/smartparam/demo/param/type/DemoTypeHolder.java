package org.smartparam.demo.param.type;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class DemoTypeHolder extends AbstractHolder {

    private String value;

    public DemoTypeHolder(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getString() {
        return value;
    }
}
