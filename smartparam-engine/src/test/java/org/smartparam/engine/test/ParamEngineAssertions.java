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
package org.smartparam.engine.test;

import org.smartparam.engine.core.repository.RepositoryItemMapAssert;
import org.smartparam.engine.core.prepared.PreparedParameterAssert;
import org.smartparam.engine.core.prepared.PreparedLevelAssert;
import org.smartparam.engine.core.parameter.ParameterEntryAssert;
import org.smartparam.engine.core.parameter.ParameterBatchLoaderAssert;
import org.smartparam.engine.core.parameter.ParameterAssert;
import org.smartparam.engine.core.output.ParamValueAssert;
import org.smartparam.engine.core.ParamEngineRuntimeConfigAssert;
import org.smartparam.engine.core.repository.MapRepositoryAssert;
import org.smartparam.engine.core.index.LevelNodeAssert;
import org.smartparam.engine.core.parameter.LevelAssert;
import org.smartparam.engine.core.repository.ItemsContainerAssert;
import org.smartparam.engine.core.context.DefaultContextAssert;
import java.util.Map;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.context.BaseParamContext;
import org.smartparam.engine.core.context.BaseParamContextAssert;
import org.smartparam.engine.core.repository.ItemsContainer;
import org.smartparam.engine.core.repository.MapRepository;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.output.ParamValue;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineAssertions extends org.fest.assertions.api.Assertions {

    public static ParameterAssert assertThat(Parameter actual) {
        return ParameterAssert.assertThat(actual);
    }

    public static LevelAssert assertThat(Level actual) {
        return LevelAssert.assertThat(actual);
    }

    public static ParameterEntryAssert assertThat(ParameterEntry actual) {
        return ParameterEntryAssert.assertThat(actual);
    }

    public static ParamValueAssert assertThat(ParamValue actual) {
        return ParamValueAssert.assertThat(actual);
    }

    public static DefaultContextAssert assertThat(DefaultContext actual) {
        return DefaultContextAssert.assertThat(actual);
    }

    public static BaseParamContextAssert assertThat(BaseParamContext actual) {
        return BaseParamContextAssert.assertThat(actual);
    }

    public static PreparedParameterAssert assertThat(PreparedParameter actual) {
        return PreparedParameterAssert.assertThat(actual);
    }

    public static PreparedLevelAssert assertThat(PreparedLevel actual) {
        return PreparedLevelAssert.assertThat(actual);
    }

    public static <T> RepositoryItemMapAssert<T> assertThatItemMap(Map<RepositoryObjectKey, T> actual) {
        return RepositoryItemMapAssert.assertThat(actual);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> container) {
        return ItemsContainerAssert.assertThat(container);
    }

    public static ParamEngineRuntimeConfigAssert assertThat(ParamEngineRuntimeConfig runtimeConfig) {
        return ParamEngineRuntimeConfigAssert.assertThat(runtimeConfig);
    }

    public static MapRepositoryAssert assertThat(MapRepository<?> repository) {
        return new MapRepositoryAssert(repository);
    }

    public static LevelNodeAssert assertThat(LevelNode<?> levelNode) {
        return LevelNodeAssert.assertThat(levelNode);
    }

    public static ParameterBatchLoaderAssert assertThat(ParameterBatchLoader actual) {
        return ParameterBatchLoaderAssert.assertThat(actual);
    }
}
