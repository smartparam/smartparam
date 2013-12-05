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

import com.google.common.base.Predicates;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.ParameterEntryBatchLoader;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.repository.fs.exception.ResourceResolverException;
import org.smartparam.repository.fs.util.StreamReaderOpener;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.util.StreamCloser;

/**
 *
 * @author Adam Dubiel
 */
public class ClasspathResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(ClasspathResourceResolver.class);

    private static final String CLASSPATH_SEPARATOR = "/";

    private static final String PACKAGE_SEPARATOR = ".";

    private final String basePath;

    private final String filePattern;

    private final ParamDeserializer deserializer;

    public ClasspathResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
        this.basePath = basePath;
        this.filePattern = filePattern;
        this.deserializer = deserializer;
    }

    @Override
    public Map<String, String> findParameterResources() {
        logger.info("scanning resources, filtering pattern: {}", filePattern);

        String packagePath = createPackagePath(basePath);
        String regexPackagePath = "^" + packagePath.replaceAll("\\" + PACKAGE_SEPARATOR, "[" + PACKAGE_SEPARATOR + "]");

        Configuration config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packagePath))
                .filterInputsBy(Predicates.containsPattern(regexPackagePath))
                .addScanners(new ResourcesScanner());
        Reflections reflections = new Reflections(config);

        Set<String> resources = reflections.getResources(Pattern.compile(filePattern));

        Map<String, String> parameters = new HashMap<String, String>();
        String parameterName, resourceName;
        for (String resource : resources) {
            resourceName = CLASSPATH_SEPARATOR + resource;
            parameterName = readParameterNameFromResource(resourceName);

            parameters.put(parameterName, resourceName);
            logger.debug("found parameter {} in resource {}", parameterName, resourceName);
        }

        return parameters;
    }

    private String createPackagePath(String basePath) {
        String packagePath = "";
        if (basePath.length() > 1) {
            packagePath = basePath.replaceAll(CLASSPATH_SEPARATOR, PACKAGE_SEPARATOR);
            if (packagePath.startsWith(PACKAGE_SEPARATOR)) {
                packagePath = packagePath.substring(1);
            }
            if (!packagePath.endsWith(PACKAGE_SEPARATOR)) {
                packagePath += PACKAGE_SEPARATOR;
            }
        }
        return packagePath;
    }

    private String readParameterNameFromResource(String resourceName) {
        try {
            return readParameterConfigFromResource(resourceName).getName();
        } catch (ParamSerializationException serializationException) {
            throw new ResourceResolverException("Unable to load parameter from " + resourceName, serializationException);
        }
    }

    private Parameter readParameterConfigFromResource(String resourceName) throws ParamSerializationException {
        BufferedReader reader = null;
        try {
            reader = StreamReaderOpener.openReaderForResource(this.getClass(), resourceName);
            return deserializer.deserializeMetadata(reader);
        } finally {
            StreamCloser.closeStream(reader);
        }
    }

    @Override
    public ParameterBatchLoader loadParameterFromResource(String parameterResourceName) {
        BufferedReader reader;
        try {
            reader = StreamReaderOpener.openReaderForResource(this.getClass(), parameterResourceName);
            Parameter metadata = deserializer.deserializeMetadata(reader);
            ParameterEntryBatchLoader entriesLoader = deserializer.deserializeEntries(reader);

            return new ParameterBatchLoader(metadata, entriesLoader);
        } catch (ParamSerializationException serializationException) {
            throw new ResourceResolverException("unable to load parameter from " + parameterResourceName, serializationException);
        }
    }
}
