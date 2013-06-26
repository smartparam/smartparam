package org.smartparam.repository.fs.resolver;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import static org.fest.assertions.api.Assertions.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.mgmt.test.mock.EditableLevelMock;
import org.smartparam.mgmt.test.mock.EditableParameterEntryMock;
import org.smartparam.mgmt.test.mock.EditableParameterMock;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import static org.smartparam.repository.fs.resolver.ResolverInegrationTestConsts.PARAMETER_SUB_DIR_NAME;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardSmartParamDeserializer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FileResourceResolverIntegrationTest extends ResolverInegrationTestConsts {

    private static final String PARAMETER_DIR_NAME = "smartParam";

    private static final String FILE_PATTERN = ".*csv$";

    private String basePath;

    private FileResourceResolver resolver;

    @DataProvider(name = "parameterResourceToNameDataProvider")
    public Object[][] parameterResourceToNameDataProvider() {
        return new Object[][]{
            {"param1", 1, createFilePath(basePath) + "param1.csv"},
            {"param2", 2, createFilePath(basePath, PARAMETER_SUB_DIR_NAME) + "param2.csv"},
            {"param3", 3, createFilePath(basePath, PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv"}
        };
    }

    @BeforeClass
    public void setUpTest() throws Exception {
        File tempPath = Files.createTempDirectory(PARAMETER_DIR_NAME).toFile();
        basePath = tempPath.toString();

        new File(tempPath, createFilePath(PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME)).mkdirs();

        File param1TmpFile = new File(tempPath, "param1.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param1.csv"), param1TmpFile);

        File param2TmpFile = new File(tempPath, createFilePath(PARAMETER_SUB_DIR_NAME) + "param2.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/" + createPath(PARAMETER_SUB_DIR_NAME) + "param2.csv"), param2TmpFile);

        File param3TmpFile = new File(tempPath, createFilePath(PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/" + createPath(PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv"), param3TmpFile);

        File param4TmpFile = new File(tempPath, "param4_ignored.txt");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param4_ignored.txt"), param4TmpFile);
    }

    @AfterClass
    public void tearDownTest() throws Exception {
        FileUtils.forceDelete(new File(basePath));
    }

    @BeforeMethod
    public void setUp() {
        SerializationConfig config = new StandardSerializationConfig();
        SmartParamDeserializer deserializer = new StandardSmartParamDeserializer(
                config,
                EditableParameterMock.class, EditableLevelMock.class, EditableParameterEntryMock.class);

        resolver = new FileResourceResolver(basePath, FILE_PATTERN, deserializer);
    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirLocationsRelativeToBasePath() throws Exception {
        Map<String, String> parameters = resolver.findParameterResources();

        assertThat(parameters).hasSize(3).contains(
                entry("param1", createFilePath(basePath) + "param1.csv"),
                entry("param2", createFilePath(basePath, PARAMETER_SUB_DIR_NAME) + "param2.csv"),
                entry("param3", createFilePath(basePath, PARAMETER_SUB_DIR_NAME, PARAMETER_DEEP_SUB_DIR_NAME) + "param3.csv"))
                .doesNotContain(entry("param4_ignored", createFilePath(basePath) + "param4_ignored.txt"));
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
