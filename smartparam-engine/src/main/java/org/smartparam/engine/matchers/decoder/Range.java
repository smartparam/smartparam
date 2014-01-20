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
package org.smartparam.engine.matchers.decoder;

/**
 *
 * @author Adam Dubiel
 */
public class Range {

    private final Object from;

    private final Object to;

    public Range(Object from, Object to) {
        this.from = from;
        this.to = to;
    }

    public static Range of(Object from, Object to) {
        return new Range(from, to);
    }

    public Object from() {
        return from;
    }

    public <T> T fromAs(Class<T> clazz) {
        return (T) from;
    }

    public Object to() {
        return to;
    }

    public <T> T toAs(Class<T> clazz) {
        return (T) to;
    }

}
