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
package org.smartparam.engine.annotations.scanner;

import org.smartparam.engine.annotated.scanner.AnnotatedObjectFactory;
import java.lang.annotation.Annotation;
import java.util.Map;
import org.testng.annotations.Test;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.testng.annotations.BeforeMethod;
import static org.smartparam.engine.test.builder.AnnotationBuilder.*;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotatedObjectFactoryTest {

    private AnnotatedObjectFactory annotatedObjectFactory;

    @BeforeMethod
    public void setUp() {
        annotatedObjectFactory = new AnnotatedObjectFactory();
    }

    @Test
    public void shouldCreateSingleObjectWithGivenValueAsKey() {
        // given
        Annotation annotation = annotation().withValue("TEST_CODE").build();

        // when
        Map<RepositoryObjectKey, Object> objects = annotatedObjectFactory.createObjects(Object.class, annotation);

        // then
        assertThatItemMap(objects).containsRepositoryKey("TEST_CODE").hasSize(1);
    }

    @Test
    public void shouldCreateSingleInstanceAndReturnItUnderDifferentKeysWhenUsingValuesProperty() {
        // given
        Annotation annotation = annotation().withValues("TEST_CODE1", "TEST_CODE2").build();

        // when
        Map<RepositoryObjectKey, Object> objects = annotatedObjectFactory.createObjects(Object.class, annotation);

        // then
        assertThatItemMap(objects).containsObjectsThatAreSame("TEST_CODE1", "TEST_CODE2").hasSize(2);
    }

    @Test
    public void shouldCreateMultipleInstancesOfObjectWhenUsingInstanceDescriptors() {
        // given
        Annotation annotation = annotation().withInstanceDescriptor("TEST_CODE1", new String[]{})
                .withInstanceDescriptor("TEST_CODE2", new String[]{"PROPERTY_VALUE"}).build();

        // when
        Map<RepositoryObjectKey, ClassWithStringConstructor> objects = annotatedObjectFactory.createObjects(ClassWithStringConstructor.class, annotation);

        // then
        assertThatItemMap(objects).containsObjectsThatAreNotSame("TEST_CODE1", "TEST_CODE2").hasSize(2);
        assertThat(objects.get(RepositoryObjectKey.withKey("TEST_CODE2")).property).isSameAs("PROPERTY_VALUE");
    }

    protected static final class ClassWithStringConstructor {

        public String property = null;

        public ClassWithStringConstructor() {
        }

        public ClassWithStringConstructor(String property) {
            this.property = property;
        }
    }
}