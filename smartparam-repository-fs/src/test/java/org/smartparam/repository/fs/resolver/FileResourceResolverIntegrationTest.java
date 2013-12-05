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
package org.smartparam.repository.fs.resolver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.repository.fs.exception.ResourceResolverException;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.config.DefaultSerializationConfig;
import org.smartparam.serializer.ParamSerializerFactory;
import org.smartparam.serializer.config.SerializationConfig;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static java.lang.System.err;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;
import static org.smartparam.engine.test.ParamEngineAssertions.entry;

/**
 *
 * @author Adam Dubiel
 */
public class FileResourceResolverIntegrationTest {

    private static final String PARAMETER_DIR_NAME = "smartParam";

    private String basePath;

    private FileResourceResolver resolver;

    @DataProvider(name = "parameterResourceToNameDataProvider")
    public Object[][] parameterResourceToNameDataProvider() {
        return new Object[][]{
            {"param1", 1, createFilePath(basePath) + "param1.csv"},
            {"param2", 2, createFilePath(basePath, "param") + "param2.csv"},
            {"param3", 3, createFilePath(basePath, "param", "deep") + "param3.csv"}
        };
    }

    private String createFilePath(String... components) {
        return StringUtils.join(components, File.separatorChar) + File.separatorChar;
    }

    @BeforeClass
    public void setUpTest() throws Exception {
        File tempPath = Files.createTempDirectory(PARAMETER_DIR_NAME).toFile();
        basePath = tempPath.toString();

        new File(tempPath, createFilePath("param", "deep")).mkdirs();

        File param1TmpFile = new File(tempPath, "param1.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param1.csv"), param1TmpFile);

        File param2TmpFile = new File(tempPath, createFilePath("param") + "param2.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param/param2.csv"), param2TmpFile);

        File param3TmpFile = new File(tempPath, createFilePath("param", "deep") + "param3.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param/deep/param3.csv"), param3TmpFile);

        File param4TmpFile = new File(tempPath, "param4_ignored.txt");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param4_ignored.txt"), param4TmpFile);
    }

    @AfterClass
    public void tearDownTest() throws Exception {
        try {
            FileUtils.forceDelete(new File(basePath));
        } catch (IOException e) {
            err.println("ignored exception: " + e);
        }
    }

    @BeforeMethod
    public void setUp() {
        SerializationConfig config = new DefaultSerializationConfig();
        ParamDeserializer deserializer = ParamSerializerFactory.paramDeserializer(config);

        resolver = new FileResourceResolver(basePath, ".*csv$", deserializer);
    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirLocationsRelativeToBasePath() throws Exception {
        // given

        // when
        Map<String, String> parameters = resolver.findParameterResources();

        // then
        assertThat(parameters).hasSize(3).contains(
                entry("param1", createFilePath(basePath) + "param1.csv"),
                entry("param2", createFilePath(basePath, "param") + "param2.csv"),
                entry("param3", createFilePath(basePath, "param", "deep") + "param3.csv"))
                .doesNotContain(entry("param4_ignored", createFilePath(basePath) + "param4_ignored.txt"));
    }

    @Test(dataProvider = "parameterResourceToNameDataProvider")
    public void shouldReturnParameterFromRepository(String parameterName, int expectedSize, String parameterResource) throws Exception {
        // given

        // when
        ParameterBatchLoader parameterBatch = resolver.loadParameterFromResource(parameterResource);

        // then
        assertThat(parameterBatch.getMetadata()).hasName(parameterName);
        assertThat(parameterBatch.getEntryLoader().nextBatch(expectedSize + 1)).hasSize(expectedSize);
    }

    @Test
    public void shouldBailIfUnresolvableResource() {
        // given

        // when
        catchException(resolver).loadParameterFromResource("WRONG_RESOURCE_NAME");

        // then
        assertThat(caughtException()).isInstanceOf(ResourceResolverException.class);
    }
}
