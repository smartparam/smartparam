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
package org.smartparam.engine.core.prepared;

import org.smartparam.engine.core.parameter.ParameterFromRepository;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 * Interface for services building complete, in-memory representation of
 * parameter (preparing parameters). Since parameter compilation is expensive,
 * cache should be used to hold compilation result.
 *
 * @see PreparedParameter
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public interface ParamPreparer {

    PreparedParameter prepare(ParameterFromRepository parameterFromRepository);

    PreparedEntry prepareIdentifiableEntry(ParameterEntry parameterEntry);
}
