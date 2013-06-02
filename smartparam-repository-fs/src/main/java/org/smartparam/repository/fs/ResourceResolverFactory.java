package org.smartparam.repository.fs;

import org.smartparam.repository.fs.resolver.FileResourceResolver;
import org.smartparam.repository.fs.resolver.ClasspathResourceResolver;
import org.smartparam.serializer.SmartParamDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ResourceResolverFactory {

    private static final String FILE_RESOLVER_PREFIX = "file://";

    private static final String CLASSPATH_RESOLVER_PREFIX = "classpath://";

    private SmartParamDeserializer deserializer;

    public ResourceResolverFactory(SmartParamDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public ResourceResolver getResourceResolver(String basePath, String filePattern) {
        if (basePath.startsWith(FILE_RESOLVER_PREFIX)) {
            return new FileResourceResolver(basePath.substring(FILE_RESOLVER_PREFIX.length()), filePattern, deserializer);
        } else if (basePath.startsWith(CLASSPATH_RESOLVER_PREFIX)) {
            return new ClasspathResourceResolver(basePath.substring(CLASSPATH_RESOLVER_PREFIX.length()), filePattern, deserializer);
        }
        throw new IllegalArgumentException("no resolver for base path: " + basePath);
    }
}
