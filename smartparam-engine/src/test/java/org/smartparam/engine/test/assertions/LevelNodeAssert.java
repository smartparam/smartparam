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
package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.index.LevelNode;

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
        Assertions.assertThat(actual.getLeafList()).hasSize(count);
        return this;
    }

    public LevelNodeAssert hasNoLeaves() {
        Assertions.assertThat(actual.getLeafList()).isNullOrEmpty();
        return this;
    }

    public LevelNodeAssert leavesEqualTo(Object... leafValues) {
        Assertions.assertThat(actual.getLeafList()).hasSize(leafValues.length);
        int index = 0;
        for (Object object : actual.getLeafList()) {
            Assertions.assertThat(object).isEqualTo(leafValues[index]);
            index++;
        }
        return this;
    }

    public LevelNodeAssert hasDirectChild(String childLevelValue) {
        actual.findNode(new String[]{childLevelValue}, 1);
        return this;
    }
}
