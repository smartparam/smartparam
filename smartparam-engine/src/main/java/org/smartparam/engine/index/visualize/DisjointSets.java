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
package org.smartparam.engine.index.visualize;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class DisjointSets {

    private List<String> existing;

    private String intersection;

    private List<String> incoming;

    public DisjointSets withPartOfSetA(String existingPart) {
        this.existing.add(existingPart);
        return this;
    }

    public DisjointSets withIntersectionOfExistingAndIncoming(String intersection) {
        this.intersection = intersection;
        return this;
    }

    public DisjointSets withPartOfSetB(String incomingPart) {
        this.existing.add(incomingPart);
        return this;
    }

    public List<String> existing() {
        return Collections.unmodifiableList(existing);
    }

    public String intersection() {
        return intersection;
    }

    public List<String> incoming() {
        return Collections.unmodifiableList(incoming);
    }
}
