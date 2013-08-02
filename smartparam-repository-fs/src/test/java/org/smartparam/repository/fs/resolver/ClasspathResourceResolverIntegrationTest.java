package org.smartparam.repository.fs.resolver;

import java.util.Map;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardParamDeserializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathResourceResolverIntegrationTest extends ResolverInegrationTestConsts {

    private ParamDeserializer deserializer;

    @DataProvider(name = "parameterResourceToNameDataProvider")
    public Object[][] parameterResourceToNameDataProvider() {
        return new Object[][]{
            {"param1", 1, "/param1.csv"},
            {"param2", 2, "/param/param2.csv"},
            {"param3", 3, "/param/deep/param3.csv"}
        };
    }

    @BeforeMethod
    public void setUp() {
        SerializationConfig config = new StandardSerializationConfig();
        deserializer = new StandardParamDeserializer(
                config,
                SimpleEditableParameter.class, SimpleEditableLevel.class, SimpleEditableParameterEntry.class);

    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirAbsoluteClasspathLocations() throws Exception {
        // given
        ClasspathResourceResolver resolver = new ClasspathResourceResolver("/", ".*csv$", deserializer);

        // when
        Map<String, String> parameters = resolver.findParameterResources();

        // then
        assertThat(parameters).hasSize(3).contains(
                entry("param1", "/param1.csv"),
                entry("param2", "/param/param2.csv"),
                entry("param3", "/param/deep/param3.csv"))
                .doesNotContain(entry("param4_ignored",  "/param4_ignored.txt"));;
    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirLocationForComplexPath() {
        // given
        ClasspathResourceResolver resolver = new ClasspathResourceResolver("/param/deep", ".*csv$", deserializer);

        // when
        Map<String, String> parameters = resolver.findParameterResources();

        // then
        assertThat(parameters).hasSize(1).contains(
                entry("param3", "/param/deep/param3.csv"));
    }

    @Test(dataProvider = "parameterResourceToNameDataProvider")
    public void shouldReturnParameterFromRepository(String parameterName, int expectedSize, String parameterResource) throws Exception {
        // given
        ClasspathResourceResolver resolver = new ClasspathResourceResolver("/", ".*csv$", deserializer);

        // when
        Parameter parameter = resolver.loadParameterFromResource(parameterResource);

        // then
        assertThat(parameter).hasName(parameterName).hasEntries(expectedSize);
    }

    @Test
    public void shouldBailIfUnresolvableResource() {
        // given
        ClasspathResourceResolver resolver = new ClasspathResourceResolver("/", ".*", deserializer);

        // when
        catchException(resolver).loadParameterFromResource("WRONG_RESOURCE_NAME");

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamResourceResolverException.class);
    }
}
