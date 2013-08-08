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
package org.smartparam.transferer.sort;

import org.smartparam.transferer.sort.SortedParameters;
import org.smartparam.transferer.sort.SimpleParameterSorter;
import java.util.Set;
import org.smartparam.transferer.TransferOperationType;
import org.smartparam.transferer.test.assertions.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.smartparam.transferer.test.builder.ParameterNameSetTestBuilder.parameterNames;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SimpleParameterSorterTest {

    private SimpleParameterSorter parameterSorter = new SimpleParameterSorter();

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void shouldAddParameterToCreateOperationIfExistingOnlyInSourceRepo() {
        // given
        Set<String> sourceRepoParameters = parameterNames().having("parameter").build();
        Set<String> targetRepoParameters = parameterNames().having("other").build();

        // when
        SortedParameters sortedParameters = parameterSorter.sort(sourceRepoParameters, targetRepoParameters);

        // then
        Assertions.assertThat(sortedParameters).containsParameter(TransferOperationType.CREATE, "parameter");
    }

    @Test
    public void shouldAddParameterToOverrideOperationIfExistingInBothRepos() {
        // given
        Set<String> sourceRepoParameters = parameterNames().having("parameter").build();
        Set<String> targetRepoParameters = parameterNames().having("parameter").build();

        // when
        SortedParameters sortedParameters = parameterSorter.sort(sourceRepoParameters, targetRepoParameters);

        // then
        Assertions.assertThat(sortedParameters).containsParameter(TransferOperationType.OVERRIDE, "parameter");
    }

    @Test
    public void shouldAddParameterToDeleteOperationIfExistingInTargetButNotInSourceRepo() {
        // given
        Set<String> sourceRepoParameters = parameterNames().having("parameter").build();
        Set<String> targetRepoParameters = parameterNames().having("other").build();

        // when
        SortedParameters sortedParameters = parameterSorter.sort(sourceRepoParameters, targetRepoParameters);

        // then
        Assertions.assertThat(sortedParameters).containsParameter(TransferOperationType.DELETE, "other");
    }
}