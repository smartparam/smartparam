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
package org.smartparam.transferer.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.repository.WritableParamRepository;
import org.smartparam.engine.core.repository.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public abstract class LoggingTransferOperation implements TransferOperation {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTransferOperation.class);

    @Override
    public void run(String parameterName, ParamRepository source, WritableParamRepository target) {
        String operationName = this.getClass().getSimpleName();

        long startTime = System.currentTimeMillis();
        logger.info("started operation {} on parameter {}", operationName, parameterName);

        performOperation(parameterName, source, target);

        long endTime = System.currentTimeMillis();
        logger.info("finished operation {} on parameter {}, took {}ms", operationName, parameterName, endTime - startTime);
    }

    protected abstract void performOperation(String parameterName, ParamRepository source, WritableParamRepository target);
}
