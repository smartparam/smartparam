package org.smartparam.engine.test.beans;

import java.util.Map;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.annotations.SmartParamJavaPlugin;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepositoryCapabilities;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamMatcher(value = AnnotatedBeanConsts.BEAN_NAME)
@SmartParamType(value = "", instances = {
    @SmartParamObjectInstance(value = AnnotatedBeanConsts.INSTANCE_ONE_NAME, constructorArgs = {"oneA", "twoA"}),
    @SmartParamObjectInstance(value = AnnotatedBeanConsts.INSTANCE_TWO_NAME, constructorArgs = {"oneB", "twoB"})})
@SmartParamFunctionInvoker(value = "", values = {AnnotatedBeanConsts.INSTANCE_ONE_NAME, AnnotatedBeanConsts.INSTANCE_TWO_NAME})
@SmartParamFunctionRepository(value = "secondary", order = AnnotatedBeanConsts.SECONDARY_TEST_ORDER)
@SmartParamDummyWithoutInstances
@SmartParamDummyWithoutValues
@SmartParamDummyWithoutValue
@SmartParamDummyWithoutOrder(value = "dummy")
public class AnnotatedBean implements FunctionRepository, Matcher, Type<AbstractHolder>, FunctionInvoker {

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

    public Map<String, Function> loadFunctions() {
        return null;
    }

    public Function loadFunction(String functionName) {
        return null;
    }

    public FunctionRepositoryCapabilities repositoryCapabilities() {
        return FunctionRepositoryCapabilities.SINGLE;
    }

    public <T extends AbstractHolder> boolean matches(String value, String pattern, Type<T> type) {
        return false;
    }

    public Object invoke(Function function, Object... args) {
        return null;
    }

    public String encode(AbstractHolder holder) {
        throw new UnsupportedOperationException("Test method without implementation.");
    }

    public AbstractHolder decode(String text) {
        throw new UnsupportedOperationException("Test method without implementation.");
    }

    public AbstractHolder convert(Object obj) {
        throw new UnsupportedOperationException("Test method without implementation.");
    }

    public AbstractHolder[] newArray(int size) {
        throw new UnsupportedOperationException("Test method without implementation.");
    }
}
