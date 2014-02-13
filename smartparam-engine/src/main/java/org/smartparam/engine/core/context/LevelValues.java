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
package org.smartparam.engine.core.context;

import java.util.Arrays;

/**
 * Simple parameter evaluation context that uses values provided directly by
 * user. Under the hood, {@link DefaultContext#setLevelValues(java.lang.Object[]) }
 * is called.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class LevelValues extends BaseParamContext {

    public LevelValues(Object... values) {
        setLevelValues(values);
    }

    public static LevelValues from(Object... values) {
        return new LevelValues(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(getLevelValues());
    }


}
