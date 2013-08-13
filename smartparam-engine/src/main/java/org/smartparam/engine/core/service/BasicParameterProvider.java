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
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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
