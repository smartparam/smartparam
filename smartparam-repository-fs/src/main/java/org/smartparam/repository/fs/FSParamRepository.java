package org.smartparam.repository.fs;

import org.smartparam.repository.fs.resolver.FileResourceResolver;
import org.smartparam.serializer.ParamDeserializer;

/**
 *
 * @author Adam Dubiel
 */
public class FSParamRepository extends AbstractFSParamRepository {

    public FSParamRepository(String basePath, String filePattern) {
        super(basePath, filePattern);
    }

    public FSParamRepository(String basePath, String filePattern, ParamDeserializer deserializer) {
        super(basePath, filePattern, deserializer);
    }

    @Override
    protected ResourceResolver createResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
        return new FileResourceResolver(basePath, filePattern, deserializer);
    }
}
