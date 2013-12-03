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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.parameter.batch.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.batch.ParameterEntryBatchLoader;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.repository.fs.exception.ResourceResolverException;
import org.smartparam.repository.fs.util.StreamReaderOpener;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.ParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class FileResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceResolver.class);

    private final String basePath;

    private final ParameterFileVisitor fileVisitor;

    private final ParamDeserializer deserializer;

    public FileResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
        this.basePath = basePath;
        this.deserializer = deserializer;
        fileVisitor = new ParameterFileVisitor(filePattern, deserializer);
    }

    @Override
    public Map<String, String> findParameterResources() {
        logger.info("scanning files at {}", basePath);
        try {
            fileVisitor.clearOldResults();

            Path basePathPath = new File(basePath).toPath();
            Files.walkFileTree(basePathPath, fileVisitor);

            return fileVisitor.getParameters();
        } catch (IOException exception) {
            throw new ResourceResolverException("Exception while scanning base path: " + basePath, exception);
        }
    }

    @Override
    public ParameterBatchLoader loadParameterFromResource(String parameterResourceName) {
        BufferedReader reader;
        try {
            reader = StreamReaderOpener.openReaderForFile(parameterResourceName, deserializer.getSerializationConfig().getCharset());
            Parameter metadata = deserializer.deserializeMetadata(reader);
            ParameterEntryBatchLoader entriesLoader = deserializer.deserializeEntries(reader);

            return new ParameterBatchLoader(metadata, entriesLoader);
        } catch (ParamSerializationException serializationException) {
            throw new ResourceResolverException("Unable to load parameter from " + parameterResourceName, serializationException);
        }
    }
}
