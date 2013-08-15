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
package org.smartparam.engine.test.scan;

import org.smartparam.engine.annotations.ObjectInstance;
import org.smartparam.engine.annotations.ParamType;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
@ParamType(value = "", instances = {
    @ObjectInstance(value = "typeInstanceOne", constructorArgs = {"PROPERTY_1_A", "PROBERTY_1_B"}),
    @ObjectInstance(value = "typeInstanceTwo", constructorArgs = {"PROBPERTY_2_A", "PROPERTY_2_B"})})
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
