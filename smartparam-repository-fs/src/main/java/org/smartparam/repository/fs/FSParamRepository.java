package org.smartparam.repository.fs;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.InitializableComponent;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardParamDeserializer;

/**
 * Repository that uses serializer/deserializer for reading parameters from
 * file system (fs). Depending on {@link ResourceResolverFactory}, it can
 * support loading files from any source. By default pure file system files
 * and classpath files scanning is available (note, that to use classpath scanning
 * you need to have reflections.org library in dependencies).
 *
 * To use files, prefix source directory with
 * <code>file://</code> (default file protocol marker).
 * To use classpath files, prefix classpath directory path with
 * <code>classpath:</code>.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParamRepository implements ParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(FSParamRepository.class);

    private String basePath;

    private String filePattern;

    private ParamDeserializer deserializer;

    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;

    private Map<String, String> parameters;

    public FSParamRepository(String basePath, String filePattern) {
        this(basePath, filePattern, null, null);
    }

    public FSParamRepository(String basePath, String filePattern, ParamDeserializer deserializer) {
        this(basePath, filePattern, deserializer, null);
    }

    public FSParamRepository(String basePath, String filePattern, ParamDeserializer deserializer, ResourceResolverFactory resourceResolverFatory) {
        this.basePath = basePath;
        this.filePattern = filePattern;
        this.deserializer = deserializer;
        this.resourceResolverFactory = resourceResolverFatory;
    }

    @Override
    public void initialize() {
        if (deserializer == null) {
            logger.debug("no custom deserializer provided, using {}", StandardParamDeserializer.class.getSimpleName());
            this.deserializer = new StandardParamDeserializer(new StandardSerializationConfig(),
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
}
