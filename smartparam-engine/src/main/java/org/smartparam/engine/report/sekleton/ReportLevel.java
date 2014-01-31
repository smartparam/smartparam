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
import org.smartparam.engine.util.Printer;

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

    ReportLevel(String value) {
        this.value = value;
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

    public void printNode(StringBuilder sb, int depth) {
        String indent = Printer.repeat(' ', depth << 2);
        boolean leaf = leaf();

        sb.append(indent).append("path : ").append(levelPath()).append(" dictionary: ").append(dictionaryOnly);
        sb.append(org.smartparam.engine.util.Formatter.NL);

        for (ReportLevel child : children.values()) {
            child.printNode(sb, depth + 1);
        }
    }

    @Override
    public String toString() {
        return "[ReportLevel " + levelPath() + " dictionary: " + dictionaryOnly + "]";
    }

    public String levelPath() {
        String lv = value != null ? value : "";
        return parent != null ? parent.levelPath() + "/" + lv : lv;
    }
}
