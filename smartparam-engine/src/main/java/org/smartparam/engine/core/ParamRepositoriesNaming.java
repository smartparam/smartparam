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
package org.smartparam.engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.repository.RepositoryName;

/**
 * Holds parameter repositories naming schema, so it is easy to retrieve parameter by name.
 *
 * @author Adam Dubiel
 */
public final class ParamRepositoriesNaming {

    private final Map<String, ParamRepository> naming = new HashMap<String, ParamRepository>();

    ParamRepositoriesNaming(List<NamedParamRepository> namedRepositories) {
        for (NamedParamRepository repository : namedRepositories) {
            naming.put(repository.name().value(), repository.repository());
        }
    }

    /**
     * Return parameter repository registered under name, or null if none found.
     */
    public ParamRepository find(RepositoryName name) {
        return naming.get(name.value());
    }

    /**
     * Return parameter repository registered under name, or null if none found.
     */
    public ParamRepository find(String name) {
        return naming.get(name);
    }

}
