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
package org.smartparam.engine.core.type;

/**
 * Contract for parameter engine type system entity. Type gives behavior to
 * its value - most of all decodes and encodes from/to String form. Each type
 * should have {@link ValueHolder} defined, which is a concrete representation
 * (instance) of type.
 *
 * There are two convenience classes that might help when creating custom types:
 * {@link AbstractType} and {@link SimpleType}. First one simplifies Type interface
 * implementation by hiding some details behind well-performing default implementations.
 * Second one makes creating a type as easy as implementing decoding/encoding
 * routine, no ValueHolder needed.
 *
 * To use custom type in engine, register it at {@link org.smartparam.engine.core.repository.TypeRepository},
 * or use {@link org.smartparam.engine.annotations.ParamType} if annotation scan
 * is enabled.
 *
 * @param H supported value holder
 *
 * @author Przemek Hertel <przemek.hertel@gamil.com>
 * @since 1.0.0
 */
public interface Type<H extends ValueHolder> {

    /**
     * Create String representation of value. Transformation has to be reversible using
     * {@link Type#decode(java.lang.String)} method:
     * <pre>
     * decode( encode(obj) ) == obj
     * </pre>
     *
     * @param holder value to stringify
     * @return string representation
     */
    String encode(H holder);

    /**
     * Create value out of provided string. This is opposite to
     * {@link Type#encode(org.smartparam.engine.core.type.AbstractHolder) }, so:
     * <pre>
     * encode( decode(str) ) == str
     * </pre>
     *
     * @param text string to interprete
     * @return value
     */
    H decode(String text);

    /**
     * Can convert any object to value of Type. Should throw IllegalArgumentException
     * if unable to convert from object.
     *
     * @param obj object to try and convert
     * @return value
     */
    H convert(Object obj);

    /**
     * Should create new, empty array of given size.
     *
     * @param size size of array
     * @return empty array of values
     */
    H[] newArray(int size);
}
