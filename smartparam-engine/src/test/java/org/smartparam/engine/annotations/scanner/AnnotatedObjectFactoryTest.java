package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.bean.RepositoryObjectKey;
import static org.smartparam.engine.test.builder.AnnotationBuilder.*;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotatedObjectFactoryTest {

    private AnnotatedObjectFactory annotatedObjectFactory;

    @Before
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