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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherDecoderRepository;
import org.smartparam.engine.core.prepared.IdentifiablePreparedEntry;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.index.CustomizableLevelIndexWalker;
import org.smartparam.engine.index.LevelLeafValuesExtractor;
import org.smartparam.engine.report.sekleton.ReportLevel;
import org.smartparam.engine.report.sekleton.ReportSkeleton;
import org.smartparam.engine.report.space.ReportLevelValuesSpaceRepository;
import org.smartparam.engine.util.ArraysUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingLevelLeafValuesExtractor implements LevelLeafValuesExtractor<PreparedEntry> {

    private static final Logger logger = LoggerFactory.getLogger(ReportingLevelLeafValuesExtractor.class);

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
        ReportingTree<PreparedEntry> reportingTree = new ReportingTree<PreparedEntry>(createLevelDescriptors(indexWalker), valueChooser);
        createTreeLevels(reportingTree);

        for (LevelNode<PreparedEntry> node : nodes) {
            PreparedEntry entry = node.getLeafValue();
            reportingTree.insertValue(Arrays.copyOf(entry.getLevels(), indexWalker.indexDepth()), entry);
        }

        logger.info(reportingTree.printTree());

        return convertPathsToEntries(reportingTree.harvestLeavesValues());
    }

    private List<ReportingTreeLevel> createLevelDescriptors(CustomizableLevelIndexWalker<PreparedEntry> indexWalker) {
        List<ReportingTreeLevel> levelDescriptors = new ArrayList<ReportingTreeLevel>();

        for (int levelIndex = 0; levelIndex < indexWalker.indexDepth(); ++levelIndex) {
            String valueSearchedInChild = null;
            String childOriginalMatcherCode = null;
            Matcher originalChildMatcher = null;
            Matcher overridenChildMatcher = null;
            Type<?> childType = null;
            if(levelIndex + 1 < indexWalker.indexDepth()) {
                childOriginalMatcherCode = indexWalker.originalMatcherCodeFor(levelIndex + 1);
                originalChildMatcher = indexWalker.originalMatcherFor(levelIndex + 1);
                overridenChildMatcher = indexWalker.matcherFor(levelIndex + 1);
                childType = indexWalker.typeFor(levelIndex + 1);
                valueSearchedInChild = indexWalker.levelValueFor(levelIndex + 1);
            }

            ReportingTreeLevel level = new ReportingTreeLevel(
                    valueSearchedInChild,
                    reportSkeleton.ambigousChildren(indexWalker.levelNameFor(levelIndex)),
                    originalChildMatcher,
                    overridenChildMatcher,
                    childType,
                    matcherDecoderRepository.getDecoder(childOriginalMatcherCode),
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
            if (childSkeletonLevel.onlyDictionaryValues()) {
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
