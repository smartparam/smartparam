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

package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.context.DefaultContext;

/**
 *
 * @author Adam Dubiel
 */
public class DefaultContextAssert extends AbstractAssert<DefaultContextAssert, DefaultContext> {

    private DefaultContextAssert(DefaultContext actual) {
        super(actual, DefaultContextAssert.class);
    }

    public static DefaultContextAssert assertThat(DefaultContext actual) {
        return new DefaultContextAssert(actual);
    }

    public DefaultContextAssert hasLevelValues(String... levelValues) {
        Assertions.assertThat(actual.getLevelValues()).containsExactly(levelValues);
        return this;
    }

    public DefaultContextAssert hasValue(String key, Object value) {
        Assertions.assertThat(actual.get(key)).isEqualTo(value);
        return this;
    }
}