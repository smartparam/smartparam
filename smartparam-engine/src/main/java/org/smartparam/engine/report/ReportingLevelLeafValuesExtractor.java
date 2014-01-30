/*
 * Copyright 2014 Adam Dubiel.
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

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.matcher.MatcherDecoderRepository;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.index.CustomizableLevelIndexWalker;
import org.smartparam.engine.index.LevelLeafValuesExtractor;
import org.smartparam.engine.report.sekleton.ReportLevel;
import org.smartparam.engine.report.sekleton.ReportSkeleton;
import org.smartparam.engine.report.space.ReportLevelValuesSpaceRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingLevelLeafValuesExtractor implements LevelLeafValuesExtractor<PreparedEntry> {

    private final MatcherDecoderRepository matcherDecoderRepository;

    private final ReportLevelValuesSpaceRepository reportLevelValuesSpaceRepository;

    private final ReportSkeleton reportSkeleton;

    private final ReportValueChooser<PreparedEntry> valueChooser;

    public ReportingLevelLeafValuesExtractor(ParamEngine paramEngine, ReportSkeleton reportSkeleton, ReportValueChooser<PreparedEntry> valueChooser) {
        ParamEngineRuntimeConfig runtimeConfig = paramEngine.runtimeConfiguration();

        this.matcherDecoderRepository = runtimeConfig.getMatcherDecoderRepository();
        this.reportLevelValuesSpaceRepository = runtimeConfig.getReportLevelValuesSpaceRepository();

        this.reportSkeleton = reportSkeleton;
        this.valueChooser = valueChooser;
    }

    @Override
    public List<PreparedEntry> extract(CustomizableLevelIndexWalker<PreparedEntry> indexWalker, List<LevelNode<PreparedEntry>> nodes) {
        ReportingTree<PreparedEntry> reportingTree = new ReportingTree<PreparedEntry>(createLevelDescriptors(indexWalker));
        createTreeLevels(reportingTree);

        for (LevelNode<PreparedEntry> node : nodes) {
            PreparedEntry entry = node.getLeafValue();
            reportingTree.insertValue(entry.getLevels(), entry);
        }

        return null;
    }

    private List<ReportingTreeLevel> createLevelDescriptors(CustomizableLevelIndexWalker<PreparedEntry> indexWalker) {
        List<ReportingTreeLevel> levelDescriptors = new ArrayList<ReportingTreeLevel>();

        for (int levelIndex = 0; levelIndex < indexWalker.indexDepth(); ++levelIndex) {
            ReportingTreeLevel level = new ReportingTreeLevel(reportSkeleton.ambiguousLevel(indexWalker.levelNameFor(levelIndex)),
                    indexWalker.matcherFor(levelIndex),
                    indexWalker.typeFor(levelIndex),
                    matcherDecoderRepository.getDecoder(indexWalker.matcherCodeFor(levelIndex)),
                    reportLevelValuesSpaceRepository.getSpaceFactory(indexWalker.matcherCodeFor(levelIndex))
            );
            levelDescriptors.add(level);
        }

        return levelDescriptors;
    }

    private void createTreeLevels(ReportingTree<PreparedEntry> reportingTree) {
        ReportingTreeNode<PreparedEntry> currentNode = reportingTree.root();
        createLevelSkeleton(currentNode, reportSkeleton.root());
    }

    private void createLevelSkeleton(ReportingTreeNode<PreparedEntry> currentNode, ReportLevel currentSkeletonLevel) {
        if (currentSkeletonLevel.leaf()) {
            return;
        }

        ReportingTreeNode<PreparedEntry> childNode;
        for (ReportLevel childSkeletonLevel : currentSkeletonLevel) {
            if (childSkeletonLevel.onlyDictionaryValues()) {
                childNode = currentNode.addDictionaryChild(childSkeletonLevel.value());
            } else {
                childNode = currentNode.addAnyChild();
            }
            createLevelSkeleton(childNode, childSkeletonLevel);
        }
    }
}
