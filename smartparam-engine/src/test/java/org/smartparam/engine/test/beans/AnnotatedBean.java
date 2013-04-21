package org.smartparam.engine.test.beans;

import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.annotations.SmartParamJavaPlugin;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamType;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamMatcher(value = AnnotatedBeanConsts.BEAN_NAME)
@SmartParamType(value = "", instances = {
    @SmartParamObjectInstance(value = AnnotatedBeanConsts.INSTANCE_ONE_NAME, constructorArgs = {"oneA", "twoA"}),
    @SmartParamObjectInstance(value = AnnotatedBeanConsts.INSTANCE_TWO_NAME, constructorArgs = {"oneB", "twoB"})})
@SmartParamFunctionInvoker(value = "", values = {AnnotatedBeanConsts.INSTANCE_ONE_NAME, AnnotatedBeanConsts.INSTANCE_TWO_NAME})
@SmartParamDummyWithoutInstances
@SmartParamDummyWithoutValues
@SmartParamDummyWithoutValue
public class AnnotatedBean {

    private String propertyOne;

    private String propertyTwo;

    public AnnotatedBean() {
    }

    public AnnotatedBean(String propertyOne, String propertyTwo) {
        this.propertyOne = propertyOne;
        this.propertyTwo = propertyTwo;
    }

    @SmartParamJavaPlugin("propertyOne")
    public String getPropertyOne() {
        return propertyOne;
    }

    @SmartParamDummyPluginWithoutValue
    public String getPropertyOneTwo() {
        return propertyOne;
    }

    @SmartParamDummyPlugin("duplicate")
    public String getPropertyTwo() {
        return propertyTwo;
    }

    @SmartParamDummyPlugin("duplicate")
    public String getPropertyTwoTwo() {
        return propertyTwo;
    }
}
