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

import org.smartparam.engine.report.tree.ReportValueChooser;
import org.smartparam.engine.report.tree.ReportingTreeLevelDescriptor;
import org.smartparam.engine.report.tree.ReportingTreeNode;
import org.smartparam.engine.report.tree.ReportingTreePath;
import org.smartparam.engine.report.tree.ReportingTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.matcher.MatcherTypeRepository;
import org.smartparam.engine.core.prepared.IdentifiablePreparedEntry;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.index.CustomizableLevelIndexWalker;
import org.smartparam.engine.index.IndexLevelDescriptor;
import org.smartparam.engine.index.LevelLeafValuesExtractor;
import org.smartparam.engine.report.skeleton.ReportLevel;
import org.smartparam.engine.report.skeleton.ReportSkeleton;
import org.smartparam.engine.report.tree.ReportLevelValuesSpaceRepository;
import org.smartparam.engine.report.tree.ReportingTreeValueDescriptor;
import org.smartparam.engine.util.ArraysUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingLevelLeafValuesExtractor implements LevelLeafValuesExtractor<PreparedEntry> {

    private static final Logger logger = LoggerFactory.getLogger(ReportingLevelLeafValuesExtractor.class);

    private final MatcherTypeRepository matcherTypeRepository;

    private final ReportLevelValuesSpaceRepository reportLevelValuesSpaceRepository;

    private final ReportSkeleton reportSkeleton;

    private final ReportValueChooser<PreparedEntry> valueChooser;

    public ReportingLevelLeafValuesExtractor(ParamEngine paramEngine, ReportSkeleton reportSkeleton, ReportValueChooser<PreparedEntry> valueChooser) {
        ParamEngineRuntimeConfig runtimeConfig = paramEngine.runtimeConfiguration();

        this.matcherTypeRepository = runtimeConfig.getMatcherTypeRepository();
        this.reportLevelValuesSpaceRepository = runtimeConfig.getReportLevelValuesSpaceRepository();

        this.reportSkeleton = reportSkeleton;
        this.valueChooser = valueChooser;
    }

    @Override
    public List<PreparedEntry> extract(CustomizableLevelIndexWalker<PreparedEntry> indexWalker, List<LevelNode<PreparedEntry>> nodes) {
        ReportingTree<PreparedEntry> reportingTree = new ReportingTree<PreparedEntry>(
                createLevelDescriptors(indexWalker),
                createOutputLevelDescriptors(indexWalker),
                valueChooser);
        createTreeLevels(reportingTree);

        int inputLevels = indexWalker.indexDepth();

        for (LevelNode<PreparedEntry> node : nodes) {
            for (PreparedEntry entry : node.getLeafList()) {
                reportingTree.insertValue(Arrays.copyOf(entry.getLevels(), inputLevels), entry);
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Reporting tree:\n{}", reportingTree.printTree());
        }

        return convertPathsToEntries(reportingTree.harvestLeavesValues());
    }

    private List<ReportingTreeLevelDescriptor> createLevelDescriptors(CustomizableLevelIndexWalker<PreparedEntry> indexWalker) {
        List<ReportingTreeLevelDescriptor> levelDescriptors = new ArrayList<ReportingTreeLevelDescriptor>();

        for (int levelIndex = 0; levelIndex < indexWalker.indexDepth(); ++levelIndex) {
            IndexLevelDescriptor descriptor = indexWalker.descriptorFor(levelIndex);
            String originalMatcherCode = descriptor.originalMatcherCode();

            ReportingTreeLevelDescriptor level = new ReportingTreeLevelDescriptor(
                    indexWalker.levelValueFor(levelIndex),
                    reportSkeleton.ambiguous(descriptor.name()),
                    descriptor.originalMatcher(),
                    descriptor.effectiveMatcher(),
                    descriptor.type(),
                    matcherTypeRepository.getMatcherType(originalMatcherCode),
                    reportLevelValuesSpaceRepository.getSpaceFactory(originalMatcherCode),
                    reportSkeleton.ambiguousLevelSpaceSetValidator(descriptor.name())
            );
            levelDescriptors.add(level);
        }

        return levelDescriptors;
    }

    private ReportingTreeValueDescriptor createOutputLevelDescriptors(CustomizableLevelIndexWalker<PreparedEntry> indexWalker) {
        ReportingTreeValueDescriptor valueDescriptor = new ReportingTreeValueDescriptor();

        for (int levelIndex = indexWalker.indexDepth(); levelIndex < indexWalker.descriptorsCount(); ++levelIndex) {
            IndexLevelDescriptor descriptor = indexWalker.descriptorFor(levelIndex);
            valueDescriptor.add(levelIndex, descriptor.name(), descriptor.type());
        }
        return valueDescriptor;
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
