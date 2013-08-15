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
public @interface ObjectInstance {

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
