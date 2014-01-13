/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.editor.model.simple;

import java.util.Arrays;
import java.util.HashSet;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleParameterTest {

    @Test
    public void shouldCopyParameterLevelsAsSimpleLevelsWhenUsingCloneConstructor() {
        // given
        SimpleParameter parameter = new SimpleParameter()
                .withLevel(new SimpleLevel().withName("level"));

        // when
        SimpleParameter clonedParameter = new SimpleParameter(parameter);

        // then
        assertThat(clonedParameter).firstLevel().isInstanceOf(SimpleLevel.class);
    }

    @Test
    public void shouldNotFailWhenClonedParameterHasNullValueAsLevelList() {
        // given
        FakeNullLevelParameter parameter = new FakeNullLevelParameter();

        // when
        SimpleParameter clonedParameter = new SimpleParameter(parameter);

        // then - not failing, yupi!
    }

    @Test
    public void shouldCopyAllParameterPropertiesWhenUsingCloneConstructor() {
        // given
        SimpleParameter parameter = new SimpleParameter()
                .withName("name").withInputLevels(10)
                .notCacheable().nullable().withArraySeparator('&');

        // when
        SimpleParameter clonedParameter = new SimpleParameter(parameter);

        // then
        assertThat(clonedParameter).hasName("name").hasInputLevels(10).isNotCacheable()
                .isNullable().hasArraySeparator('&');
    }

    @Test
    public void shouldNotCopyAnyParameterEntriesWhenUsingCloneContructor() {
        // given
        SimpleParameter parameter = new SimpleParameter();
        parameter.setEntries(new HashSet<ParameterEntry>(Arrays.asList(new SimpleParameterEntry())));

        // when
        SimpleParameter clonedParameter = new SimpleParameter(parameter);

        // then
        assertThat(clonedParameter).hasNoEntries();
    }

}
