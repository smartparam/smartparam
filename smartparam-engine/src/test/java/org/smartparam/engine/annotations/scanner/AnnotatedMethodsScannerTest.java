package org.smartparam.engine.annotations.scanner;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.Test;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.test.beans.AnnotatedBeanConsts;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;
import org.smartparam.engine.annotations.SmartParamJavaPlugin;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.beans.SmartParamDummyPlugin;
import org.smartparam.engine.test.beans.SmartParamDummyPluginWithoutValue;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class AnnotatedMethodsScannerTest {

    private PackageList buildPackageList() {
        PackageList packageList = new PackageList();
        packageList.addPackage(AnnotatedBeanConsts.TEST_PACKAGE);
        return packageList;
    }

    @Test
    public void testSimpleCase() {
        AnnotatedMethodsScanner scanner = new AnnotatedMethodsScanner();
        Map<String, Method> methods = scanner.getAnnotatedMethods(buildPackageList(), SmartParamJavaPlugin.class);

        assertThat(methods).hasSize(1);
        assertTrue(methods.containsKey("propertyOne"));
    }


    @Test
    public void testScan_duplicateFunctions() {
        AnnotatedMethodsScanner scanner = new AnnotatedMethodsScanner();
        try {
        Map<String, Method> methods = scanner.getAnnotatedMethods(buildPackageList(), SmartParamDummyPlugin.class);
        }
        catch(SmartParamException exception) {
            assertEquals(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE, exception.getErrorCode());
            return;
        }
        fail();
    }

    @Test
    public void testScan_annotationWithoutValue() {
        AnnotatedMethodsScanner scanner = new AnnotatedMethodsScanner();
        try {
        Map<String, Method> methods = scanner.getAnnotatedMethods(buildPackageList(), SmartParamDummyPluginWithoutValue.class);
        }
        catch(SmartParamException exception) {
            assertEquals(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception.getErrorCode());
            return;
        }
        fail();
    }

}
