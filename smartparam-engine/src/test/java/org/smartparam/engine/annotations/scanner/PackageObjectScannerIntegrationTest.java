package org.smartparam.engine.annotations.scanner;

import java.util.Map;
import org.smartparam.engine.annotations.ParamMatcher;
import org.smartparam.engine.annotations.ParamType;
import org.smartparam.engine.annotations.ParamFunctionInvoker;
import org.smartparam.engine.annotations.ParamFunctionRepository;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.test.scan.annotation.DummyAnnotationWithoutInstances;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.PackageListTestBuilder.*;
import static com.googlecode.catchexception.CatchException.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.scan.annotation.DummyAnnotationWithoutOrder;
import org.smartparam.engine.test.scan.annotation.DummyAnnotationWithoutValue;
import org.smartparam.engine.test.scan.annotation.DummyAnnotationWithoutValues;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class PackageObjectScannerIntegrationTest {

    private static final String TEST_PACKAGE = "org.smartparam.engine.test.scan";

    private PackageList packageList;

    @BeforeMethod
    public void setUp() {
        packageList = packageList().withPackage(TEST_PACKAGE).build();
    }

    @Test
    public void shouldFindSingleBeanWhenScanningForParamMatchers() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamMatcher.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsRepositoryKey("dummyMatcher").hasSize(1);
    }

    @Test
    public void shouldCreateTwoInstancesOfSingleBeanClass() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamType.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsObjectsThatAreNotSame("typeInstanceOne", "typeInstanceTwo").hasSize(2);
    }

    @Test
    public void shouldRegisterSingleBeanUnderMultipleNames() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamFunctionInvoker.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsObjectsThatAreSame("nameOne", "nameTwo").hasSize(2);
    }

    @Test
    public void shouldReturnBeansInOrder() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        Map<RepositoryObjectKey, Object> foundObjects = scanner.getAnnotatedObjects(ParamFunctionRepository.class, packageList);

        // then
        assertThatItemMap(foundObjects).containsRepositoryKeys("primaryRepository", "secondaryRepsitory");
    }

    @Test
    public void shouldFailToScanAnnotationWithoutInstancesMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutInstances.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutValueMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutValue.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutValuesMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutValues.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }

    @Test
    public void shouldFailToScanAnnotationWithoutOrderMethod() {
        // given
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>();

        // when
        catchException(scanner).getAnnotatedObjects(DummyAnnotationWithoutOrder.class, packageList);

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamException.class);
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }
}
