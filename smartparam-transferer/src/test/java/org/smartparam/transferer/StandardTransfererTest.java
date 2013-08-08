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
package org.smartparam.transferer;

import java.util.EnumMap;
import java.util.Map;
import org.smartparam.engine.core.repository.EditableParamRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.transferer.operation.TransferOperation;
import org.smartparam.transferer.sort.ParameterSorter;
import org.smartparam.transferer.sort.SortedParameters;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.transferer.test.builder.SortedParametersTestBuilder.sortedParameters;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardTransfererTest {

    private StandardTransferer standardTransferer;

    private Map<TransferOperationType, TransferOperation> operations;

    private ParameterSorter sorter;

    @BeforeMethod
    public void setUp() {
        sorter = mock(ParameterSorter.class);

        operations = new EnumMap<TransferOperationType, TransferOperation>(TransferOperationType.class);
        operations.put(TransferOperationType.CREATE, mock(TransferOperation.class));
        operations.put(TransferOperationType.DELETE, mock(TransferOperation.class));
        operations.put(TransferOperationType.OVERRIDE, mock(TransferOperation.class));

        standardTransferer = new StandardTransferer(sorter, operations);
    }

    @Test
    public void shouldRunTransferOperationsOnlyForChosenOperations() {
        // given
        TransferConfig config = new TransferConfig(TransferOperationType.CREATE);
        SortedParameters sortedParameters = sortedParameters().with(TransferOperationType.CREATE, "create").build();
        when(sorter.sort(anySetOf(String.class), anySetOf(String.class))).thenReturn(sortedParameters);

        // when
        standardTransferer.transfer(config, mock(ParamRepository.class), mock(EditableParamRepository.class));

        // then
        verify(operations.get(TransferOperationType.CREATE), times(1)).run(eq("create"), any(ParamRepository.class), any(EditableParamRepository.class));
        verify(operations.get(TransferOperationType.DELETE), never()).run(anyString(), any(ParamRepository.class), any(EditableParamRepository.class));
        verify(operations.get(TransferOperationType.OVERRIDE), never()).run(anyString(), any(ParamRepository.class), any(EditableParamRepository.class));
    }
}