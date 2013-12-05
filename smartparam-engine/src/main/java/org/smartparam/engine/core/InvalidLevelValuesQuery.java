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
package org.smartparam.engine.core;

import java.util.Arrays;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 * When using {@link org.smartparam.engine.core.context.LevelValues} and
 * provided less level values than there are levels defined in parameter.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class InvalidLevelValuesQuery extends SmartParamException {

    InvalidLevelValuesQuery(Object[] levelValues, int parameterLevelCount) {
        super("INVALID_LEVEL_VALUES_QUERY",
                String.format("Level values array length differs from parameter input levels count (%d != %d). Provided values: %s.",
                        levelValues.length, parameterLevelCount, Arrays.toString(levelValues)));
    }

}
