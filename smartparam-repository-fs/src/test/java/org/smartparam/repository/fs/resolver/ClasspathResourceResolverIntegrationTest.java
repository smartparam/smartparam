package org.smartparam.repository.fs.resolver;

import java.util.Map;
import static org.fest.assertions.api.Assertions.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import static org.smartparam.repository.fs.resolver.ResolverInegrationTestConsts.PARAMETER_SUB_DIR_NAME;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardSmartParamDeserializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathResourceResolverIntegrationTest extends ResolverInegrationTestConsts {

    private static final String BASE_PATH = "/";

    private static final String FILE_PATTERN = ".*csv$";

    private ClasspathResourceResolver resolver;

    @DataProvider(name = "parameterResourceToNameDataProvider")
    public Object[][] parameterResourceToNameDataProvider() {
        return new Object[][]{
            {"param1", 1, createPath() + "param1.csv"},
            {"param2", 2, BASE_PATH + createPath(PARAMETER_SUB_DIR_NAME) + "param2.csv"},
            {"param3", 3, BASE_PATH + createPath(PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv"}
        };
    }

    @BeforeMethod
    public void setUp() {
        SerializationConfig config = new StandardSerializationConfig();
        SmartParamDeserializer deserializer = new StandardSmartParamDeserializer(
                config,
                SimpleEditableParameter.class, SimpleEditableLevel.class, SimpleEditableParameterEntry.class);

        resolver = new ClasspathResourceResolver(BASE_PATH, FILE_PATTERN, deserializer);
    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirLocationsRelativeToBasePath() throws Exception {
        Map<String, String> parameters = resolver.findParameterResources();

        assertThat(parameters).hasSize(3).contains(
                entry("param1", createPath() + "param1.csv"),
                entry("param2", BASE_PATH + createPath(PARAMETER_SUB_DIR_NAME) + "param2.csv"),
                entry("param3", BASE_PATH + createPath(PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv"))
                .doesNotContain(entry("param4_ignored", createPath() + "param4_ignored.txt"));;
    }

    @Test(dataProvider = "parameterResourceToNameDataProvider")
    public void shouldReturnParameterFromRepository(String parameterName, int expectedSize, String parameterResource) throws Exception {
        Parameter parameter = resolver.loadParameterFromResource(parameterResource);
        assertThat(parameter.getName()).isEqualTo(parameterName);
        assertThat(parameter.getEntries()).hasSize(expectedSize);
    }

    @Test(expectedExceptions = SmartParamResourceResolverException.class)
    public void shouldBailIfUnresolvableResource() {
        resolver.loadParameterFromResource("WRONG_RESOURCE_NAME");
    }
}
