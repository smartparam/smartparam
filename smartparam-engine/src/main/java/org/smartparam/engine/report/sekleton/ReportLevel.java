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
package org.smartparam.engine.report.sekleton;

import java.util.*;
import org.smartparam.engine.core.index.Star;

/**
 *
 * @author Adam Dubiel
 */
public class ReportLevel implements Iterable<ReportLevel> {

    private ReportLevel parent;

    private final Map<String, ReportLevel> children = new LinkedHashMap<String, ReportLevel>();

    private boolean dictionaryOnly = true;

    private String value;

    public ReportLevel() {
    }

    public static ReportLevel level() {
        return new ReportLevel();
    }

    public ReportLevel withChild(ReportLevel child) {
        dictionaryOnly = false;
        return withChild(Star.SYMBOL, child);
    }

    public ReportLevel withChild(String value, ReportLevel child) {
        child.parent = this;
        children.put(value, child);
        child.value = value;
        return this;
    }

    @Override
    public Iterator<ReportLevel> iterator() {
        return children.values().iterator();
    }

    public boolean leaf() {
        return children.isEmpty();
    }

    public String value() {
        return value;
    }

    public boolean onlyDictionaryValues() {
        return dictionaryOnly;
    }
}
