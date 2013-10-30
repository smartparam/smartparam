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
package org.smartparam.engine.core.assembler;

import java.util.Collection;
import java.util.HashSet;
import org.smartparam.engine.core.engine.MultiValue;
import org.smartparam.engine.core.engine.ParamValue;

/**
 * @author Adam Dubiel
 */
public class BasicObjectAssembler implements ObjectAssembler {

    private AssemblyStrategy assemblyStrategy;

    public BasicObjectAssembler(AssemblyStrategy assemblyStrategy) {
        this.assemblyStrategy = assemblyStrategy;
    }

    @Override
    public <T> T assemble(Class<T> valueObjectClass, MultiValue values) {
        return assemblyStrategy.assemble(valueObjectClass, values);
    }

    @Override
    public <T> Collection<T> assemble(Class<T> valueObjectClass, ParamValue matrix) {
        Collection<T> assemblyCollection = new HashSet<T>();
        for (MultiValue row : matrix.rows()) {
            assemblyCollection.add(assemble(valueObjectClass, row));
        }
        return assemblyCollection;
    }
}
