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
package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.ItemsContainer;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.engine.ParamValue;
import org.smartparam.engine.core.engine.PreparedLevel;
import org.smartparam.engine.core.engine.PreparedParameter;
import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public class Assertions extends org.fest.assertions.api.Assertions {

    public static ParameterAssert assertThat(Parameter actual) {
        return ParameterAssert.assertThat(actual);
    }

    public static LevelAssert assertThat(Level actual) {
        return LevelAssert.assertThat(actual);
    }

    public static ParamValueAssert assertThat(ParamValue actual) {
        return ParamValueAssert.assertThat(actual);
    }

    public static DefaultContextAssert assertThat(DefaultContext actual) {
        return DefaultContextAssert.assertThat(actual);
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

    public static SmartParamExceptionAssert assertThat(SmartParamException exception) {
        return SmartParamExceptionAssert.assertThat(exception);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> container) {
        return ItemsContainerAssert.assertThat(container);
    }

    public static ParamEngineAssert assertThat(SmartParamEngine engine) {
        return ParamEngineAssert.assertThat(engine);
    }

    public static MapRepositoryAssert assertThat(MapRepository<?> repository) {
        return new MapRepositoryAssert(repository);
    }

    public static LevelNodeAssert assertThat(LevelNode<?> levelNode) {
        return LevelNodeAssert.assertThat(levelNode);
    }
}
