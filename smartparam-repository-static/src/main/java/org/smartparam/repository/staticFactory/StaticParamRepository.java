/*
 * Copyright 2013 the original author or authors.
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
package org.smartparam.repository.staticFactory;

import java.util.List;
import org.smartparam.engine.annotations.scanner.TypeScanner;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.TypeScanningRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * Repository, that uses factory classes to produce {@link Parameter} instances
 * that are used in parameter engine. Can be useful when static typing is important,
 * like when using parameter as a flow dispatcher that calls different plugin depending
 * on input levels.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StaticParamRepository implements ParamRepository, TypeScanningRepository {

    private MapRepository<StaticParameterFactory> registerers = new MapRepository<StaticParameterFactory>(StaticParameterFactory.class);

    public StaticParamRepository() {
    }

    @Override
    public void scanAnnotations(TypeScanner scanner) {
        List<StaticParameterFactory> registererList = scanner.scanTypesWithoutName(ScannableStaticParameterFactory.class);
        for(StaticParameterFactory registerer : registererList) {
            register(registerer);
        }
    }

    @Override
    public Parameter load(String parameterName) {
        Parameter parameter = null;
        if (registerers.contains(parameterName)) {
            parameter = retrieveParameterFromRegisterer(parameterName);
        }
        return parameter;
    }

    private Parameter retrieveParameterFromRegisterer(String parameterName) {
        List<Parameter> parameters = registerers.getItem(parameterName).createParameters();
        for(Parameter parameter : parameters) {
            if(parameter.getName().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
    }

    public void register(StaticParameterFactory registerer) {
        for (Parameter parameter : registerer.createParameters()) {
            registerers.registerUnique(parameter.getName(), registerer);
        }
    }

    @Override
    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support non-cacheable parameters");
    }
}
