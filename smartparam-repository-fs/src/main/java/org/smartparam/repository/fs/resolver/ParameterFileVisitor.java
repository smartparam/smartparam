package org.smartparam.repository.fs.resolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterFileVisitor extends SimpleFileVisitor<Path> {

    private static final Logger logger = LoggerFactory.getLogger(ParameterFileVisitor.class);

    private SmartParamDeserializer deserializer;

    private Pattern filePattern;

    private Map<String, String> parameters = new HashMap<String, String>();

    public ParameterFileVisitor(String filePattern, SmartParamDeserializer deserializer) {
        this.filePattern = Pattern.compile(filePattern);
        this.deserializer = deserializer;
    }

    public void clearOldResults() {
        parameters.clear();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String fileName = file.toFile().getCanonicalPath();
        if(!filePattern.matcher(fileName).matches() ) {
            logger.debug("discarding file {}, does not match filtering pattern: {}", fileName, filePattern);
            return FileVisitResult.CONTINUE;
        }

        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(file, deserializer.getSerializationConfig().getCharset());
            Parameter parameter = deserializer.deserializeConfig(reader);
            parameters.put(parameter.getName(), fileName);

            logger.debug("found parameter {} in file {}", parameter.getName(), fileName);
        } catch (SmartParamSerializationException exception) {
            throw new IOException(exception);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return FileVisitResult.CONTINUE;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
