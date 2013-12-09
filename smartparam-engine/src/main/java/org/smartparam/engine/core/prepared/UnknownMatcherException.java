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

import org.smartparam.engine.core.exception.SmartParamException;

/**
 * Trying to use unknown level {@link org.smartparam.engine.core.index.Matcher}.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class UnknownMatcherException extends SmartParamException {

    public UnknownMatcherException(String levelName, String matcherCode) {
        super("UNKNOWN_TYPE",
                String.format("Level %s has unknown matcher %s. "
                        + "To see all registered matchers, look for MapRepository logs on INFO level during startup.",
                        levelName, matcherCode));
    }

}
