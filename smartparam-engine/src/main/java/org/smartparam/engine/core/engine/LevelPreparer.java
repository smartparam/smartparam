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
package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.model.Level;

/**
 *
 * @author Adam Dubiel
 * @since 0.9.0
 */
public interface LevelPreparer {

    PreparedLevel prepare(Level level);

    MatcherRepository getMatcherRepository();

    void setMatcherRepository(MatcherRepository matcherRepository);

    TypeRepository getTypeRepository();

    void setTypeRepository(TypeRepository typeRepository);

    FunctionProvider getFunctionProvider();

    void setFunctionProvider(FunctionProvider functionProvider);
}
