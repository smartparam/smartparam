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
package org.smartparam.engine.core.index;

import org.assertj.core.api.AbstractAssert;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.test.ParamEngineAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class LevelNodeAssert extends AbstractAssert<LevelNodeAssert, LevelNode<?>> {

    private LevelNodeAssert(LevelNode<?> actual) {
        super(actual, LevelNodeAssert.class);
    }

    public static LevelNodeAssert assertThat(LevelNode<?> actual) {
        return new LevelNodeAssert(actual);
    }

    public LevelNodeAssert hasLeaves(int count) {
        ParamEngineAssertions.assertThat(actual.getLeafList()).hasSize(count);
        return this;
    }

    public LevelNodeAssert hasNoLeaves() {
        ParamEngineAssertions.assertThat(actual.getLeafList()).isNullOrEmpty();
        return this;
    }

    public LevelNodeAssert leavesEqualTo(Object... leafValues) {
        ParamEngineAssertions.assertThat(actual.getLeafList()).hasSize(leafValues.length);
        int index = 0;
        for (Object object : actual.getLeafList()) {
            ParamEngineAssertions.assertThat(object).isEqualTo(leafValues[index]);
            index++;
        }
        return this;
    }

    public LevelNodeAssert hasDirectChild(String childLevelValue) {
        actual.findNode(new String[]{childLevelValue}, 1);
        return this;
    }
}
