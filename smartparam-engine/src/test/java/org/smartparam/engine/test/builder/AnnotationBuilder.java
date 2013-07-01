package org.smartparam.engine.test.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.annotations.SmartParamObjectInstance;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotationBuilder {

    private String value;

    private String[] values = new String[] {};

    private List<SmartParamObjectInstance> instanceDescriptors = new ArrayList<SmartParamObjectInstance>();

    private int order;

    public static AnnotationBuilder annotation() {
        return new AnnotationBuilder();
    }

    public Annotation build() {
        return new SmartParamFunctionRepository() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public String[] values() {
                return values;
            }

            @Override
            public SmartParamObjectInstance[] instances() {
                return instanceDescriptors.toArray(new SmartParamObjectInstance[instanceDescriptors.size()]);
            }

            @Override
            public int order() {
                return order;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SmartParamFunctionRepository.class;
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
        SmartParamObjectInstance instanceDescriptor = new SmartParamObjectInstance() {
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
                return SmartParamObjectInstance.class;
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
