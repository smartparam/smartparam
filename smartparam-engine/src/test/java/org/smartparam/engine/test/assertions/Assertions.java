package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.ItemsContainer;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.ParamValue;
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

    public static <T> RepositoryItemMapAssert<T> assertThatItemMap(Map<RepositoryObjectKey, T> actual) {
        return RepositoryItemMapAssert.assertThat(actual);
    }

    public static SmartParamExceptionAssert assertThat(SmartParamException exception) {
        return SmartParamExceptionAssert.assertThat(exception);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> container) {
        return ItemsContainerAssert.assertThat(container);
    }

    public static ParamEngineAssert assertThat(ParamEngine engine) {
        return ParamEngineAssert.assertThat(engine);
    }

    public static MapRepositoryAssert assertThat(MapRepository<?> repository) {
        return new MapRepositoryAssert(repository);
    }

    public static LevelNodeAssert assertThat(LevelNode<?> levelNode) {
        return LevelNodeAssert.assertThat(levelNode);
    }

}
