package org.smartparam.repository.fs.resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.fs.ResourceResolver;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FileResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceResolver.class);

    private String basePath;

    private ParameterFileVisitor fileVisitor;

    private ParamDeserializer deserializer;

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
            throw new SmartParamResourceResolverException("exception while scanning base path: " + basePath, exception);
        }
    }

    @Override
    public ParameterBatchLoader loadParameterFromResource(String parameterResourceName) {
        File file = new File(parameterResourceName);
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(file.toPath(), deserializer.getSerializationConfig().getCharset());

            Parameter metadata = deserializer.deserializeConfig(reader);
            ParameterEntryBatchLoader entriesLoader = deserializer.deserializeEntries(reader);

            return new ParameterBatchLoader(metadata, entriesLoader);
        } catch (IOException ioException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + parameterResourceName, ioException);
        } catch (SmartParamSerializationException serializationException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + parameterResourceName, serializationException);
        } finally {
            closeReader(reader);
        }
    }

    private void closeReader(Reader reader) throws SmartParamResourceResolverException {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException exception) {
            throw new SmartParamResourceResolverException("eception while loading file stream", exception);
        }
    }

    private Parameter readFromFile(File file) throws IOException, SmartParamSerializationException {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(file.toPath(), deserializer.getSerializationConfig().getCharset());
            return deserializer.deserialize(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
