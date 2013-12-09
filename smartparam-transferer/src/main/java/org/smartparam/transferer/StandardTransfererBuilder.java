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

import org.smartparam.transferer.operation.TransferOperationType;
import java.util.EnumMap;
import java.util.Map;
import org.smartparam.transferer.operation.CreateParameter;
import org.smartparam.transferer.operation.DeleteParameter;
import org.smartparam.transferer.operation.OverrideParameter;
import org.smartparam.transferer.operation.TransferOperation;
import org.smartparam.transferer.sort.ParameterSorter;
import org.smartparam.transferer.sort.OperationParameterSorter;

/**
 *
 * @author Adam Dubiel
 */
public final class StandardTransfererBuilder {

    private ParameterSorter sorter;

    private final Map<TransferOperationType, TransferOperation> operations = new EnumMap<TransferOperationType, TransferOperation>(TransferOperationType.class);

    private StandardTransfererBuilder() {
    }

    public static StandardTransfererBuilder standardTransferer() {
        return new StandardTransfererBuilder();
    }

    public StandardTransferer build() {
        if (sorter == null) {
            sorter = new OperationParameterSorter();
        }
        if (!operations.containsKey(TransferOperationType.CREATE)) {
            operations.put(TransferOperationType.CREATE, new CreateParameter());
        }
        if (!operations.containsKey(TransferOperationType.OVERRIDE)) {
            operations.put(TransferOperationType.OVERRIDE, new OverrideParameter());
        }
        if (!operations.containsKey(TransferOperationType.DELETE)) {
            operations.put(TransferOperationType.DELETE, new DeleteParameter());
        }

        return new StandardTransferer(sorter, operations);
    }

    public StandardTransfererBuilder withParameterSorter(ParameterSorter sorter) {
        this.sorter = sorter;
        return this;
    }

    public StandardTransfererBuilder usingOperation(TransferOperationType operationType, TransferOperation operation) {
        operations.put(operationType, operation);
        return this;
    }
}
