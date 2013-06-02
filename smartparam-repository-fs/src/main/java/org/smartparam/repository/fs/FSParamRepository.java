package org.smartparam.repository.fs;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.fs.model.FSLevel;
import org.smartparam.repository.fs.model.FSParameter;
import org.smartparam.repository.fs.model.FSParameterEntry;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardSmartParamDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParamRepository implements ParamRepository {

    private String basePath;

    private String filePattern;

    private SmartParamDeserializer deserializer;

    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;

    private Map<String, String> parameters;

    public FSParamRepository(String basePath, String filePattern) {
        this.basePath = basePath;
        this.filePattern = filePattern;
    }

    @PostConstruct
    public void initialize() {
        if(deserializer == null) {
            this.deserializer = new StandardSmartParamDeserializer(new StandardSerializationConfig(),
                FSParameter.class, FSLevel.class, FSParameterEntry.class);
        }
        if(resourceResolverFactory == null) {
            this.resourceResolverFactory = new ResourceResolverFactory(deserializer);
        }

        resourceResolver = resourceResolverFactory.getResourceResolver(basePath, filePattern);
        parameters = resourceResolver.findParameterResources();
    }

    @Override
    public Parameter load(String parameterName) {
        String parameterResourceName = parameters.get(parameterName);
        if (parameterResourceName != null) {
            return resourceResolver.loadParameterFromResource(parameterResourceName);
        }
        return null;
    }

    @Override
    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support non-cacheable parameters");
    }

    public void setDeserializer(SmartParamDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public void setResourceResolverFactory(ResourceResolverFactory resourceResolverFactory) {
        this.resourceResolverFactory = resourceResolverFactory;
    }
}
