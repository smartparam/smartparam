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
package org.smartparam.engine.core.context;

import org.testng.annotations.Test;

import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 * @author Przemek Hertel
 */
public class LevelValuesTest {

    @Test
    public void shouldSetupExplicitLevelValuesWithoutAnyChange() {

        // given
        Object[] criteria = {"A", 7};

        // when
        LevelValues ctx = new LevelValues(criteria);

        // then
        assertThat(ctx).hasLevelValues("A", 7);
    }

}
