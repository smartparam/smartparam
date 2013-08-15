package org.smartparam.engine.annotations.scanner;

import java.lang.reflect.Method;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.annotations.JavaPlugin;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotation;
import org.smartparam.engine.test.scan.plugins.DummyPluginAnnotationWithoutValue;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.PackageListTestBuilder.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel
 */
public class PackageMethodScannerIntegrationTest {

    private static final String TEST_PACKAGE = "org.smartparam.engine.test.scan.plugins";

    private PackageList packageList;

    @BeforeMethod
    public void setUp() {
        packageList = packageList().withPackage(TEST_PACKAGE).build();
    }

    @Test
    public void shouldScanMethodsAndReturnAllAnnotatedWithType() {
        // given
        PackageMethodScanner scanner = new PackageMethodScanner(packageList);

        // when
        Map<String, Method> methods = scanner.scanMethods(JavaPlugin.class);

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
