package org.smartparam.engine.annotations.scanner;

import java.util.Map;
import org.junit.Test;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.PackageList;

import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;

import org.smartparam.engine.test.beans.AnnotatedBean;
import org.smartparam.engine.test.beans.AnnotatedBeanConsts;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.beans.SmartParamDummyWithoutInstances;
import org.smartparam.engine.test.beans.SmartParamDummyWithoutValue;
import org.smartparam.engine.test.beans.SmartParamDummyWithoutValues;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class AnnotatedObjectsScannerTest {

    private PackageList buildPackageList() {
        PackageList packageList = new PackageList();
        packageList.addPackage(AnnotatedBeanConsts.TEST_PACKAGE);
        return packageList;
    }

    @Test
    public void testScanSimpleBeanMap() {
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>(buildPackageList());
        Map<String, Object> foundObjects = scanner.getAnnotatedObjects(SmartParamMatcher.class);

        assertThat(foundObjects).hasSize(1);
        assertThat(foundObjects.keySet()).contains(AnnotatedBeanConsts.BEAN_NAME);
    }

    @Test
    public void testScanBeanWithMultipleInstances() {
        AnnotatedObjectsScanner<AnnotatedBean> scanner = new AnnotatedObjectsScanner<AnnotatedBean>(buildPackageList());
        Map<String, AnnotatedBean> foundObjects = scanner.getAnnotatedObjects(SmartParamType.class);

        assertThat(foundObjects).hasSize(2);
        assertThat(foundObjects.keySet()).contains(AnnotatedBeanConsts.INSTANCE_ONE_NAME, AnnotatedBeanConsts.INSTANCE_TWO_NAME);

        AnnotatedBean instance = foundObjects.get(AnnotatedBeanConsts.INSTANCE_ONE_NAME);
        assertThat(instance.getPropertyOne()).isEqualTo("oneA");
        assertThat(instance.getPropertyTwo()).isEqualTo("twoA");

        instance = foundObjects.get(AnnotatedBeanConsts.INSTANCE_TWO_NAME);
        assertThat(instance.getPropertyOne()).isEqualTo("oneB");
        assertThat(instance.getPropertyTwo()).isEqualTo("twoB");
    }

    @Test
    public void testScanBeanWithMultipleValues() {
        AnnotatedObjectsScanner<AnnotatedBean> scanner = new AnnotatedObjectsScanner<AnnotatedBean>(buildPackageList());
        Map<String, AnnotatedBean> foundObjects = scanner.getAnnotatedObjects(SmartParamFunctionInvoker.class);

        assertThat(foundObjects).hasSize(2);
        assertThat(foundObjects.keySet()).contains(AnnotatedBeanConsts.INSTANCE_ONE_NAME, AnnotatedBeanConsts.INSTANCE_TWO_NAME);

        assertSame(foundObjects.get(AnnotatedBeanConsts.INSTANCE_ONE_NAME), foundObjects.get(AnnotatedBeanConsts.INSTANCE_TWO_NAME));
    }

    @Test
    public void testScanBrokenAnnotation_withoutInstances() {
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>(buildPackageList());

        try {
            scanner.getAnnotatedObjects(SmartParamDummyWithoutInstances.class);
        } catch (SmartParamException exception) {
            assertEquals(exception.getErrorCode(), SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR);
            return;
        }
        fail();
    }

    @Test
    public void testScanBrokenAnnotation_withoutValues() {
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>(buildPackageList());

        try {
            scanner.getAnnotatedObjects(SmartParamDummyWithoutValues.class);
        } catch (SmartParamException exception) {
            assertEquals(exception.getErrorCode(), SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR);
            return;
        }
        fail();
    }

    @Test
    public void testScanBrokenAnnotation_withoutValue() {
        AnnotatedObjectsScanner<Object> scanner = new AnnotatedObjectsScanner<Object>(buildPackageList());

        try {
            scanner.getAnnotatedObjects(SmartParamDummyWithoutValue.class);
        } catch (SmartParamException exception) {
            assertEquals(exception.getErrorCode(), SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR);
            return;
        }
        fail();
    }
}
