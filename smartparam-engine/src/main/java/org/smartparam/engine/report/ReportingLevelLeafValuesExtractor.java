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
import java.util.Arrays;
import java.util.List;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.matcher.MatcherTypeRepository;
import org.smartparam.engine.core.prepared.IdentifiablePreparedEntry;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.index.CustomizableLevelIndexWalker;
import org.smartparam.engine.index.LevelLeafValuesExtractor;
import org.smartparam.engine.report.skeleton.ReportLevel;
import org.smartparam.engine.report.skeleton.ReportSkeleton;
import org.smartparam.engine.report.space.ReportLevelValuesSpaceRepository;
import org.smartparam.engine.util.ArraysUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingLevelLeafValuesExtractor implements LevelLeafValuesExtractor<PreparedEntry> {

    private final MatcherTypeRepository matcherDecoderRepository;

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
        ReportingTree<PreparedEntry> reportingTree = new ReportingTree<PreparedEntry>(createLevelDescriptors(indexWalker), valueChooser);
        createTreeLevels(reportingTree);

        int inputLevels = indexWalker.indexDepth();

        for (LevelNode<PreparedEntry> node : nodes) {
            for(PreparedEntry entry : node.getLeafList()) {
                reportingTree.insertValue(Arrays.copyOf(entry.getLevels(), inputLevels), entry);
            }
        }

        return convertPathsToEntries(reportingTree.harvestLeavesValues());
    }

    private List<ReportingTreeLevel> createLevelDescriptors(CustomizableLevelIndexWalker<PreparedEntry> indexWalker) {
        List<ReportingTreeLevel> levelDescriptors = new ArrayList<ReportingTreeLevel>();

        for (int levelIndex = 0; levelIndex < indexWalker.indexDepth(); ++levelIndex) {
            String childOriginalMatcherCode = indexWalker.originalMatcherCodeFor(levelIndex);

            ReportingTreeLevel level = new ReportingTreeLevel(
                    indexWalker.levelValueFor(levelIndex),
                    reportSkeleton.ambigousChildren(indexWalker.levelNameFor(levelIndex)),
                    indexWalker.originalMatcherFor(levelIndex),
                    indexWalker.matcherFor(levelIndex),
                    indexWalker.typeFor(levelIndex),
                    matcherDecoderRepository.getMatcherType(childOriginalMatcherCode),
                    reportLevelValuesSpaceRepository.getSpaceFactory(childOriginalMatcherCode)
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
            if (currentSkeletonLevel.onlyDictionaryValues()) {
                childNode = currentNode.addDictionaryChild(childSkeletonLevel.value());
            } else {
                childNode = currentNode.addAnyChild();
            }
            createLevelSkeleton(childNode, childSkeletonLevel);
        }
    }

    private List<PreparedEntry> convertPathsToEntries(List<ReportingTreePath<PreparedEntry>> paths) {
        List<PreparedEntry> entries = new ArrayList<PreparedEntry>();
        for (ReportingTreePath<PreparedEntry> path : paths) {
            entries.add(createEntry(path));
        }

        return entries;
    }

    private PreparedEntry createEntry(ReportingTreePath<PreparedEntry> path) {
        PreparedEntry originalEntry = path.value();

        String[] entryValues = ArraysUtil.concat(path.segmentsArray(), extractOutputValues(path.length(), originalEntry));

        if (originalEntry instanceof IdentifiablePreparedEntry) {
            IdentifiablePreparedEntry identifiableEntry = (IdentifiablePreparedEntry) originalEntry;
            return new IdentifiablePreparedEntry(identifiableEntry.getKey(), entryValues);
        } else {
            return new PreparedEntry(entryValues);
        }
    }

    private String[] extractOutputValues(int inputLevels, PreparedEntry entry) {
        return Arrays.copyOfRange(entry.getLevels(), inputLevels, entry.getLevels().length);
    }
}
