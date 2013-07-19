package org.smartparam.repository.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.repository.fs.resolver.FileResourceResolver;
import org.smartparam.repository.fs.resolver.ClasspathResourceResolver;
import org.smartparam.serializer.ParamDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ResourceResolverFactory {

    private static final Logger logger = LoggerFactory.getLogger(ResourceResolverFactory.class);

    private static final String FILE_RESOLVER_PREFIX = "file://";

    private static final String CLASSPATH_RESOLVER_PREFIX = "classpath:";

    private ParamDeserializer deserializer;

    public ResourceResolverFactory(ParamDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public ResourceResolver getResourceResolver(String basePath, String filePattern) {
        ResourceResolver resolver = null;
        String effectiveBasePath;

        if (basePath.startsWith(FILE_RESOLVER_PREFIX)) {
            effectiveBasePath = trimBasePath(basePath, FILE_RESOLVER_PREFIX);
            resolver = new FileResourceResolver(effectiveBasePath, filePattern, deserializer);
        } else if (basePath.startsWith(CLASSPATH_RESOLVER_PREFIX)) {
            effectiveBasePath = trimBasePath(basePath, CLASSPATH_RESOLVER_PREFIX);
            resolver = new ClasspathResourceResolver(effectiveBasePath, filePattern, deserializer);
        }

        if (resolver == null) {
            throw new IllegalArgumentException("no resolver for base path: " + basePath);
        }

        logger.info("using {} to resolve resoures from path {}", resolver.getClass().getSimpleName(), basePath);
        return resolver;
    }

    private String trimBasePath(String basePath, String prefixToRemove) {
        return basePath.substring(prefixToRemove.length());
    }
}
