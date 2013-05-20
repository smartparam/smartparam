package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Descriptor of object instance, can pass constructor arguments. Constructor
 * arguments are constrained to string type.
 *
 * For example, take class P:
 * <pre>
 * class P {
 *     P() { ... };
 *     P(String a) { ... };
 *     P(String a, String b) { ... };
 *     P(String a, int b) { ... }; // can't instantiate using instance descriptor!
 * }
 * </pre>
 * Instance descriptors to create 3 instances of object P, each
 * using different constructor are:
 * <pre>
 *
 * @SmartParamObjectInstance(value = "no-arg", constructorArgs = {})
 * @SmartParamObjectInstance(value = "a", constructorArgs = {"someAValue"})
 * @SmartParamObjectInstance(value = "ab", constructorArgs = {"someAValue", "someBValue"})
 * </pre>
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartParamObjectInstance {

    /**
     * Name of instance.
     *
     * @return name
     */
    String value();

    /**
     * String array of constructor arguments, class has to have matching
     * constructor.
     *
     * @return arguments
     */
    String[] constructorArgs();
}
