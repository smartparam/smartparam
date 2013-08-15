package org.smartparam.repository.fs.resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
import org.smartparam.repository.fs.util.StreamReaderOpener;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
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
        BufferedReader reader;
        try {
            reader = StreamReaderOpener.openReaderForFile(parameterResourceName, deserializer.getSerializationConfig().getCharset());
            Parameter metadata = deserializer.deserializeConfig(reader);
            ParameterEntryBatchLoader entriesLoader = deserializer.deserializeEntries(reader);

            return new ParameterBatchLoader(metadata, entriesLoader);
        } catch (SmartParamSerializationException serializationException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + parameterResourceName, serializationException);
        }
    }
}
