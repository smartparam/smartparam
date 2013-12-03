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
package org.smartparam.engine.core.repository;

import org.smartparam.engine.annotated.repository.ScanningTypeRepository;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.Test;
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
        ScanningTypeRepository typeRepository = typeRepository().withType("TYPE", new StringType()).build();

        // when
        catchException(typeRepository).register("TYPE", null);
        SmartParamException exception = (SmartParamException) caughtException();

        // then
        assertThat(exception).hasErrorCode(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE);
    }
}
