package org.smartparam.repository.fs;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardSmartParamDeserializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParamRepository implements ParamRepository {

    private static final Logger logger = LoggerFactory.getLogger(FSParamRepository.class);

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
        if (deserializer == null) {
            logger.debug("no custom deserializer provided, using {}", StandardSmartParamDeserializer.class.getSimpleName());
            this.deserializer = new StandardSmartParamDeserializer(new StandardSerializationConfig(),
                    SimpleEditableParameter.class, SimpleEditableLevel.class, SimpleEditableParameterEntry.class);
        }
        if (resourceResolverFactory == null) {
            logger.debug("no custom resource resolver factory provided, using {}", ResourceResolverFactory.class.getSimpleName());
            this.resourceResolverFactory = new ResourceResolverFactory(deserializer);
        }

        resourceResolver = resourceResolverFactory.getResourceResolver(basePath, filePattern);
        parameters = resourceResolver.findParameterResources();

        logger.info("found {} parameters after scanning resources at {}", parameters.size(), basePath);
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
