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
package org.smartparam.editor.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.engine.core.parameter.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ParamRepositoryNaming {

    private final Map<Class<? extends ParamRepository>, List<RepositoryName>> registeredNames = new HashMap<Class<? extends ParamRepository>, List<RepositoryName>>();

    ParamRepositoryNaming() {
    }

    public static ParamRepositoryNaming empty() {
        return new ParamRepositoryNaming();
    }

    void register(Class<? extends ParamRepository> repositoryClass, String... names) {
        if (!registeredNames.containsKey(repositoryClass)) {
            registeredNames.put(repositoryClass, new ArrayList<RepositoryName>());
        }
        List<RepositoryName> namesOfRepository = registeredNames.get(repositoryClass);
        for (String name : names) {
            namesOfRepository.add(new RepositoryName(name));
        }
    }

    public boolean hasCustomNameFor(Class<? extends ParamRepository> repositoryClass) {
        return registeredNames.containsKey(repositoryClass);
    }

    public RepositoryName name(Class<? extends ParamRepository> repositoryClass) {
        if (!hasCustomNameFor(repositoryClass)) {
            return new RepositoryName(repositoryClass.getSimpleName());
        }
        return registeredNames.get(repositoryClass).get(0);
    }

    public boolean hasCustomNameFor(Class<? extends ParamRepository> repositoryClass, int repositoryOccurence) {
        List<RepositoryName> namesOfRepository = registeredNames.get(repositoryClass);
        return namesOfRepository != null && namesOfRepository.size() > repositoryOccurence;
    }

    public RepositoryName name(Class<? extends ParamRepository> repositoryClass, int repositoryOccurence) {
        if (!hasCustomNameFor(repositoryClass, repositoryOccurence)) {
            return new RepositoryName(repositoryClass.getSimpleName() + (repositoryOccurence > 0 ? repositoryOccurence : ""));
        }
        List<RepositoryName> namesOfRepository = registeredNames.get(repositoryClass);
        return namesOfRepository.get(repositoryOccurence);
    }
}
