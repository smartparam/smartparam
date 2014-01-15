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
package org.smartparam.editor.core.store;

import org.smartparam.engine.core.parameter.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamRepositoryNamingBuilder {

    private final ParamRepositoryNaming repositoryNaming = new ParamRepositoryNaming();

    private ParamRepositoryNamingBuilder() {
    }

    public static ParamRepositoryNamingBuilder repositoryNaming() {
        return new ParamRepositoryNamingBuilder();
    }

    public ParamRepositoryNaming build() {
        return repositoryNaming;
    }

    public ParamRepositoryNamingBuilder registerAs(Class<? extends ParamRepository> repositoryClass, String... names) {
        repositoryNaming.register(repositoryClass, names);
        return this;
    }
}
