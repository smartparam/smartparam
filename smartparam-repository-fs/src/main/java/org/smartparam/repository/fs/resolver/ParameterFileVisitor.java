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
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.ParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterFileVisitor extends SimpleFileVisitor<Path> {

    private static final Logger logger = LoggerFactory.getLogger(ParameterFileVisitor.class);

    private final ParamDeserializer deserializer;

    private final Pattern filePattern;

    private final Map<String, String> parameters = new HashMap<String, String>();

    public ParameterFileVisitor(String filePattern, ParamDeserializer deserializer) {
        this.filePattern = Pattern.compile(filePattern);
        this.deserializer = deserializer;
    }

    public void clearOldResults() {
        parameters.clear();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String fileName = file.toFile().getCanonicalPath();
        if (!filePattern.matcher(fileName).matches()) {
            logger.debug("discarding file {}, does not match filtering pattern: {}", fileName, filePattern);
            return FileVisitResult.CONTINUE;
        }

        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(file, deserializer.getSerializationConfig().getCharset());
            Parameter parameter = deserializer.deserializeMetadata(reader);
            parameters.put(parameter.getName(), fileName);

            logger.debug("found parameter {} in file {}", parameter.getName(), fileName);
        } catch (ParamSerializationException exception) {
            throw new IOException(exception);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return FileVisitResult.CONTINUE;
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }
}
