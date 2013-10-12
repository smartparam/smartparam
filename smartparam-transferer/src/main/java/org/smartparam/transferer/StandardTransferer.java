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

import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.repository.WritableParamRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.transferer.operation.TransferOperation;
import org.smartparam.transferer.sort.ParameterSorter;
import org.smartparam.transferer.sort.SortedParameters;

/**
 *
 * @author Adam Dubiel
 */
public class StandardTransferer implements Transferer {

    private static final Logger logger = LoggerFactory.getLogger(StandardTransferer.class);

    private ParameterSorter sorter;

    private Map<TransferOperationType, TransferOperation> operations;

    public StandardTransferer(ParameterSorter sorter, Map<TransferOperationType, TransferOperation> operations) {
        this.sorter = sorter;
        this.operations = operations;
    }

    @Override
    public void transfer(TransferConfig config, ParamRepository source, WritableParamRepository target) {
        long startTime = System.currentTimeMillis();
        logger.info("starting transfer from repository {} to repository {}", source.getClass().getSimpleName(), target.getClass().getSimpleName());

        SortedParameters sortedParameters = sorter.sort(source.listParameters(), target.listParameters());
        runTransferOperations(sortedParameters, config, source, target);

        long endTime = System.currentTimeMillis();
        logger.info("done transfering parameters, took {}ms", endTime - startTime);
    }

    private void runTransferOperations(SortedParameters parameters, TransferConfig config, ParamRepository source, WritableParamRepository target) {
        for (TransferOperationType operationType : config.getOperationsToPerform()) {
            runOperationOnParameters(operations.get(operationType), parameters.getParameterNames(operationType), source, target);
        }
    }

    private void runOperationOnParameters(TransferOperation operation, Set<String> parameters, ParamRepository source, WritableParamRepository target) {
        for (String parameter : parameters) {
            operation.run(parameter, source, target);
        }
    }
}
