package org.smartparam.engine.test.scan;

import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamType(value = "", instances = {
    @SmartParamObjectInstance(value = "typeInstanceOne", constructorArgs = {"PROPERTY_1_A", "PROBERTY_1_B"}),
    @SmartParamObjectInstance(value = "typeInstanceTwo", constructorArgs = {"PROBPERTY_2_A", "PROPERTY_2_B"})})
public class DummyType implements Type {

    private String propertyOne;

    private String propertyTwo;

    public DummyType(String propertyOne, String propertyTwo) {
        this.propertyOne = propertyOne;
        this.propertyTwo = propertyTwo;
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    public String getPropertyTwo() {
        return propertyTwo;
    }

    @Override
    public String encode(AbstractHolder holder) {
        throw new UnsupportedOperationException("Dummy implementation");
    }

    @Override
    public AbstractHolder decode(String text) {
        throw new UnsupportedOperationException("Dummy implementation");
    }

    @Override
    public AbstractHolder convert(Object obj) {
        throw new UnsupportedOperationException("Dummy implementation");
    }

    @Override
    public AbstractHolder[] newArray(int size) {
        throw new UnsupportedOperationException("Dummy implementation");
    }
}
