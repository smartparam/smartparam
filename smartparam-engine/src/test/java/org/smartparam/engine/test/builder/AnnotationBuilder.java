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
package org.smartparam.engine.test.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.annotations.ParamFunctionRepository;
import org.smartparam.engine.annotations.ObjectInstance;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotationBuilder {

    private String value;

    private String[] values = new String[] {};

    private List<ObjectInstance> instanceDescriptors = new ArrayList<ObjectInstance>();

    private int order;

    public static AnnotationBuilder annotation() {
        return new AnnotationBuilder();
    }

    public Annotation build() {
        return new ParamFunctionRepository() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public String[] values() {
                return values;
            }

            @Override
            public ObjectInstance[] instances() {
                return instanceDescriptors.toArray(new ObjectInstance[instanceDescriptors.size()]);
            }

            @Override
            public int order() {
                return order;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ParamFunctionRepository.class;
            }
        };
    }

    public AnnotationBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public AnnotationBuilder withValues(String... values) {
        this.values = values;
        return this;
    }

    public AnnotationBuilder withInstanceDescriptor(final String name, final String[] constructorArgs) {
        ObjectInstance instanceDescriptor = new ObjectInstance() {
            @Override
            public String value() {
                return name;
            }

            @Override
            public String[] constructorArgs() {
                return constructorArgs;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ObjectInstance.class;
            }
        };
        instanceDescriptors.add(instanceDescriptor);

        return this;
    }

    public AnnotationBuilder withOrder(int order) {
        this.order = order;
        return this;
    }
}
