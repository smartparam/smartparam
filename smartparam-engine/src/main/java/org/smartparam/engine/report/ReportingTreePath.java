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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreePath<T> {

    private final List<String> segments = new ArrayList<String>();

    private final T value;

    public ReportingTreePath(String[] segments, T value) {
        this.segments.addAll(Arrays.asList(segments));
        this.value = value;
    }

    public ReportingTreePath(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    public String segmentAt(int index) {
        return segments.get(index);
    }

    public int length() {
        return segments.size();
    }

    public void addSegment(String value) {
        this.segments.add(value);
    }

    public void pushSegment(String value) {
        this.segments.add(0, value);
    }

    public List<String> segments() {
        return Collections.unmodifiableList(segments);
    }

    public String[] segmentsArray() {
        return segments.toArray(new String[segments.size()]);
    }
}
