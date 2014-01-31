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
package org.smartparam.engine.report.skeleton;

import java.util.*;

/**
 *
 * @author Adam Dubiel
 */
public class ReportSkeleton implements Iterable<ReportLevel> {

    private static final int TO_STRING_LENGTH = 100;

    private final ReportLevel rootLevel = new ReportLevel("ROOT");

    private final Set<String> ambiguousLevels = new HashSet<String>();

    public ReportSkeleton() {
    }

    public static ReportSkeleton reportSkeleton() {
        return new ReportSkeleton();
    }

    public ReportSkeleton withAmbigousLevel(String levelName) {
        ambiguousLevels.add(levelName);
        return this;
    }

    public ReportSkeleton withAmbigousLevels(String... levelNames) {
        ambiguousLevels.addAll(Arrays.asList(levelNames));
        return this;
    }

    public ReportSkeleton withRootLevel(ReportLevel level) {
        rootLevel.withChild(level);
        return this;
    }

    public ReportSkeleton withLevel(String key, ReportLevel level) {
        rootLevel.withChild(key, level);
        return this;
    }

    public ReportLevel root() {
        return rootLevel;
    }

    @Override
    public Iterator<ReportLevel> iterator() {
        return rootLevel.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TO_STRING_LENGTH);
        rootLevel.printNode(builder, 0);
        return builder.toString();
    }

    public boolean ambigousChildren(String levelName) {
        return ambiguousLevels.contains(levelName);
    }
}
