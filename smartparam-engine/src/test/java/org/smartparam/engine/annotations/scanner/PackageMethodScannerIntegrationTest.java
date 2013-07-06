package org.smartparam.engine.annotations.scanner;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.annotations.SmartParamJavaPlugin;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotation;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotationWithoutValue;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.PackageListTestBuilder.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class PackageMethodScannerIntegrationTest {

    private static final String TEST_PACKAGE = "org.smartparam.engine.test.scan.plugins";

    private PackageList packageList;

    @Before
    public void setUp() {
        packageList = packageList().withPackage(TEST_PACKAGE).build();
    }

    @Test
    public void shouldScanMethodsAndReturnAllAnnotatedWithType() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        Map<String, Method> methods = scanner.scanMethods(SmartParamJavaPlugin.class);

        // then
        assertThat(methods).hasSize(1).containsKey("javaPlugin");
    }

    @Test
    public void shouldFailWhenRegisteringTwoPluginsWithSameName() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        catchException(scanner).scanMethods(DummyPluginAnnotation.class);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).isNotNull().hasErrorCode(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE);
    }

    @Test
    public void shouldFailWhenPluginAnnotationHasNoValueMethod() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        catchException(scanner).scanMethods(DummyPluginAnnotationWithoutValue.class);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).isNotNull().hasErrorCode(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR);
    }
}
