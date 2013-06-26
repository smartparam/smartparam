package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.ItemsContainer;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class Assertions extends org.fest.assertions.api.Assertions {

    public static RepositoryItemMapAssert assertThatItemMap(Map<RepositoryObjectKey, Object> actual) {
        return new RepositoryItemMapAssert(actual);
    }

    public static SmartParamExceptionAssert assertThat(SmartParamException exception) {
        return new SmartParamExceptionAssert(exception);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> container) {
        return new ItemsContainerAssert(container);
    }

    public static ParamEngineAssert assertThat(ParamEngine engine) {
        return new ParamEngineAssert(engine);
    }

}
