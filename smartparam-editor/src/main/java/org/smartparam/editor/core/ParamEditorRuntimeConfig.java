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
package org.smartparam.editor.core;

import java.util.Collections;
import java.util.Map;
import org.smartparam.editor.core.matcher.MatcherAwareConverter;
import org.smartparam.editor.core.store.ParamRepositoryNaming;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEditorRuntimeConfig {

    private final Map<String, MatcherAwareConverter<?>> matcherConverters;

    private final ParamRepositoryNaming repositoryNaming;

    public ParamEditorRuntimeConfig(Map<String, MatcherAwareConverter<?>> matcherConverters,
            ParamRepositoryNaming repositoryNaming) {
        this.matcherConverters = Collections.unmodifiableMap(matcherConverters);
        this.repositoryNaming = repositoryNaming;
    }

    public Map<String, MatcherAwareConverter<?>> matcherConverters() {
        return matcherConverters;
    }

    public ParamRepositoryNaming repositoryNaming() {
        return repositoryNaming;
    }
}
