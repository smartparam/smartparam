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

import java.util.Map;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.repository.fs.exception.ResourceResolverException;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.config.DefaultSerializationConfig;
import org.smartparam.serializer.ParamSerializerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel
 */
public class ClasspathResourceResolverIntegrationTest {

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
        SerializationConfig config = new DefaultSerializationConfig();
        deserializer = ParamSerializerFactory.paramDeserializer(config);
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
                .doesNotContain(entry("param4_ignored", "/param4_ignored.txt"));;
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
        ParameterBatchLoader parameterLoader = resolver.loadParameterFromResource(parameterResource);

        // then
        assertThat(parameterLoader.getMetadata()).hasName(parameterName);
        assertThat(parameterLoader.getEntryLoader().nextBatch(expectedSize + 1)).hasSize(expectedSize);
    }

    @Test
    public void shouldBailIfUnresolvableResource() {
        // given
        ClasspathResourceResolver resolver = new ClasspathResourceResolver("/", ".*", deserializer);

        // when
        catchException(resolver).loadParameterFromResource("WRONG_RESOURCE_NAME");

        // then
        assertThat(caughtException()).isInstanceOf(ResourceResolverException.class);
    }
}
