package org.smartparam.engine.core.repository;

import org.junit.*;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.types.string.StringType;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;
import static org.smartparam.engine.test.builder.SmartTypeRepositoryTestBuilder.*;

/**
 * @author Przemek Hertel
 */
public class SmartTypeRepositoryTest {

    @Test
    public void shouldFailWhenTryingToRegisterMultipleItemsUnderSameName() {
        // given
        SmartTypeRepository typeRepository = typeRepository().withType("TYPE", new StringType()).build();

        // when
        catchException(typeRepository).register("TYPE", null);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).hasErrorCode(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE);
    }
}
