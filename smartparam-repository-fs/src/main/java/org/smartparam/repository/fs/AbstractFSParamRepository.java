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
package org.smartparam.repository.fs;

import org.smartparam.repository.fs.resolver.ResourceResolver;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.initialization.InitializableComponent;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.StandardParamDeserializer;
import org.smartparam.serializer.config.DefaultSerializationConfig;
import org.smartparam.serializer.ParamSerializerFactory;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractFSParamRepository implements ParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFSParamRepository.class);

    private static final int DEFAULT_BATCH_LOADER_SIZE = 2000;

    private String basePath;

    private String filePattern;

    private ParamDeserializer deserializer;

    private ResourceResolver resourceResolver;

    private Map<String, String> parameters;

    public AbstractFSParamRepository(String basePath, String filePattern) {
        this(basePath, filePattern, null);
    }

    public AbstractFSParamRepository(String basePath, String filePattern, ParamDeserializer deserializer) {
        this.basePath = basePath;
        this.filePattern = filePattern;
        this.deserializer = deserializer;
    }

    @Override
    public void initialize() {
        if (deserializer == null) {
            logger.debug("no custom deserializer provided, using {}", StandardParamDeserializer.class.getSimpleName());
            this.deserializer = ParamSerializerFactory.paramDeserializer(new DefaultSerializationConfig());
        }

        resourceResolver = createResourceResolver(basePath, filePattern, deserializer);
        parameters = resourceResolver.findParameterResources();

        logger.info("found {} parameters after scanning resources at {}", parameters.size(), basePath);
    }

    protected abstract ResourceResolver createResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer);

    @Override
    public Parameter load(String parameterName) {
        String parameterResourceName = parameters.get(parameterName);
        if (parameterResourceName != null) {
            return resourceResolver.loadParameterFromResource(parameterResourceName);
        }
        return null;
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        String parameterResourceName = parameters.get(parameterName);
        if (parameterResourceName != null) {
            return resourceResolver.batchLoadParameterFromResource(parameterResourceName);
        }
        return null;
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        logger.info("trying to load parameter {}, but {} does not support non-cacheable parameters", parameterName, getClass().getSimpleName());
        return null;
    }

    @Override
    public Set<String> listParameters() {
        return parameters.keySet();
    }
}
