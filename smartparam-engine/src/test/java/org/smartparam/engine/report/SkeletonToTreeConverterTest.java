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
import org.testng.annotations.Test;
import static org.smartparam.engine.report.skeleton.ReportLevel.level;
import static org.smartparam.engine.report.tree.ReportingTreeAssert.assertThat;
import static org.smartparam.engine.report.tree.ReportingTreeBuilder.reportingTree;

/**
 *
 * @author Adam Dubiel
 */
public class SkeletonToTreeConverterTest {

    private static final Logger logger = LoggerFactory.getLogger(SkeletonToTreeConverterTest.class);

    private final SkeletonToTreeConverter skeletonToTreeConverter = new SkeletonToTreeConverter();

    @Test
    public void shouldCreateLevelsInsideReportingTreeBasedOnSkeletonWithMatchingDictionaryOnlyFlag() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(4).build();

        // ROOT/*/A/*
        ReportSkeleton skeleton = ReportSkeleton.reportSkeleton()
                .withLevel(level()
                        .withChild("A",
                                level()
                        )
                );

        logger.debug(skeleton.toString());

        // when
        skeletonToTreeConverter.createTreeLevels(tree, skeleton);

        // then
        assertThat(tree).hasDepth(4).levelAt(1).isDictionaryLevel();
    }

    @Test
    public void shouldCreateLevelsInsideReportingTreeWhenMultipleDictionaryLevelsStack() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(4).build();

        // ROOT/FIRST/SECOND_A/*/*
        ReportSkeleton skeleton = ReportSkeleton.reportSkeleton();
        skeleton.withLevel(
                "FIRST", ReportLevel.level().withChild(
                        "SECOND_A", ReportLevel.level().withChild(
                                ReportLevel.level().withChild(
                                        ReportLevel.level()
                                )
                        )
                )
        );
        logger.debug(skeleton.toString());

        // when
        skeletonToTreeConverter.createTreeLevels(tree, skeleton);

        // then
        assertThat(tree).hasDepth(4)
                .levelAt(0).isDictionaryLevel()
                .levelAt(1).isDictionaryLevel()
                .levelAt(2).isNotDictionaryLevel();
    }
}
