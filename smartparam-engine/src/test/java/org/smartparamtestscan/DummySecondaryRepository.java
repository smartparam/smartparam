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
package org.smartparamtestscan;

import org.smartparam.engine.annotations.ParamFunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 */
@ParamFunctionRepository(value = "secondaryRepsitory", order = 2)
public class DummySecondaryRepository implements FunctionRepository {

    @Override
    public Function loadFunction(String functionName) {
        throw new UnsupportedOperationException("Dummy implementation");
    }
}
