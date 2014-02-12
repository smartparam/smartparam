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
package org.smartparam.engine.report.tree;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.smartparam.engine.test.Iterables;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeAssert extends AbstractAssert<ReportingTreeAssert, ReportingTree<?>> {

    private ReportingTreeNode<?> currentLevel;

    private ReportingTreeAssert(ReportingTree<?> actual) {
        super(actual, ReportingTreeAssert.class);
    }

    public static ReportingTreeAssert assertThat(ReportingTree<?> actual) {
        return new ReportingTreeAssert(actual);
    }

    public ReportingTreeAssert hasDepth(int depth) {
        Assertions.assertThat(actual.height()).isEqualTo(depth);
        return this;
    }

    public ReportingTreeAssert levelAt(int depth) {
        ReportingTreeNode<?> currentNode = actual.root();
        for (int index = 0; index < depth; ++index) {
            currentNode = Iterables.firstItem(currentNode.allChildren());
        }
        currentLevel = currentNode;
        return this;
    }

    public ReportingTreeAssert isDictionaryLevel() {
        Assertions.assertThat(currentLevel.dictionaryOnly()).overridingErrorMessage("Expected level %s to be a dictionary level.", currentLevel).isTrue();
        return this;
    }
}
