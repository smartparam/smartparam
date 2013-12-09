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

import org.smartparam.transferer.sort.SortedParameters;
import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.test.ParamEngineAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class SortedParametersAssert extends AbstractAssert<SortedParametersAssert, SortedParameters> {

    private SortedParametersAssert(SortedParameters actual) {
        super(actual, SortedParametersAssert.class);
    }

    public static SortedParametersAssert assertThat(SortedParameters actual) {
        return new SortedParametersAssert(actual);
    }

    public SortedParametersAssert containsParameter(TransferOperationType operationType, String paramterName) {
        ParamEngineAssertions.assertThat(actual.getParameterNames(operationType)).contains(paramterName);
        return this;
    }
}