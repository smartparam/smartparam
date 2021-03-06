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
package org.smartparam.engine.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.report.skeleton.ReportLevel;
import org.smartparam.engine.report.skeleton.ReportSkeleton;
import org.smartparam.engine.report.tree.ReportingTree;
import org.smartparam.engine.report.tree.ReportingTreeNode;

/**
 *
 * @author Adam Dubiel
 */
class SkeletonToTreeConverter {

    private static final Logger logger = LoggerFactory.getLogger(SkeletonToTreeConverter.class);

    void createTreeLevels(ReportingTree<?> tree, ReportSkeleton skeleton) {
        ReportingTreeNode<?> currentNode = tree.root();
        createLevelSkeleton(currentNode, skeleton.root());

        if (logger.isTraceEnabled()) {
            logger.trace(skeleton.toString());
        }
    }

    private void createLevelSkeleton(ReportingTreeNode<?> currentNode, ReportLevel currentSkeletonLevel) {
        if (!currentSkeletonLevel.onlyDictionaryValues()) {
            currentNode.allowAnyValues();
        }

        if (currentSkeletonLevel.leaf()) {
            return;
        }

        ReportingTreeNode<?> childNode;
        for (ReportLevel childSkeletonLevel : currentSkeletonLevel) {
            childNode = currentNode.child(childSkeletonLevel.value());
            createLevelSkeleton(childNode, childSkeletonLevel);
        }
    }
}
