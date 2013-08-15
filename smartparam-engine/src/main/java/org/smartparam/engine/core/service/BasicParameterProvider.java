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
package org.smartparam.engine.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterProvider implements ParameterProvider {

    private MapRepository<ParamRepository> innerRepository = new MapRepository<ParamRepository>(ParamRepository.class, new TreeMap<RepositoryObjectKey, ParamRepository>());

    @Override
    public Parameter load(String parameterName) {
        Parameter parameter = null;
        for (ParamRepository repository : innerRepository.getItemsOrdered().values()) {
            parameter = repository.load(parameterName);
            if (parameter != null) {
                break;
            }
        }
        return parameter;
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        Set<ParameterEntry> entries = null;
        for (ParamRepository repository : innerRepository.getItemsOrdered().values()) {
            entries = repository.findEntries(parameterName, levelValues);
            if (entries != null) {
                break;
            }
        }
        return entries;
    }

    @Override
    public void register(String type, int index, ParamRepository object) {
        innerRepository.register(new RepositoryObjectKey(type, index), object);
    }

    @Override
    public Map<String, ParamRepository> registeredItems() {
        return innerRepository.getItemsOrdered();
    }

    @Override
    public void registerAll(Map<String, ParamRepository> objects) {
        innerRepository.registerAllOrdered(objects);
    }

    @Override
    public void registerAll(List<ParamRepository> repositories) {
        int order = 10;
        for(ParamRepository repository : repositories) {
            innerRepository.register(new RepositoryObjectKey(repository.getClass().getSimpleName(), order), repository);
            order++;
        }
    }


}
