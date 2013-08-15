/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.transferer.test.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterNameSetTestBuilder {

    private Set<String> parameters = new HashSet<String>();

    private ParameterNameSetTestBuilder() {
    }

    public static ParameterNameSetTestBuilder parameterNames() {
        return new ParameterNameSetTestBuilder();
    }

    public Set<String> build() {
        return parameters;
    }

    public ParameterNameSetTestBuilder having(String... parameterNames) {
        parameters.addAll(Arrays.asList(parameterNames));
        return this;
    }
}
