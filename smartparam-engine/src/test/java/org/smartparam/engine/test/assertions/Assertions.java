package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.ItemsContainer;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class Assertions extends org.fest.assertions.api.Assertions {

    public static ParameterAssert assertThat(Parameter actual) {
        return ParameterAssert.assertThat(actual);
    }

    public static LevelAssert assertThat(Level actual) {
        return LevelAssert.assertThat(actual);
    }

    public static <T> RepositoryItemMapAssert<T> assertThatItemMap(Map<RepositoryObjectKey, T> actual) {
        return new RepositoryItemMapAssert<T>(actual);
    }

    public static SmartParamExceptionAssert assertThat(SmartParamException exception) {
        return new SmartParamExceptionAssert(exception);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> container) {
        return new ItemsContainerAssert(container);
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
