/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.output.MultiValue;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.types.date.DateHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.string.StringHolder;

/**
 *
 * @author Adam Dubiel
 */
public final class MultiValueBuilder {

    private final List<Object> values = new ArrayList<Object>();

    private final Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private MultiValueBuilder() {
    }

    public static MultiValueBuilder multiValue() {
        return new MultiValueBuilder();
    }

    public MultiValue build() {
        return new MultiValue(values.toArray(), indexMap);
    }

    public MultiValueBuilder withNamedLevels(Map<String, Integer> indexMap) {
        this.indexMap.putAll(indexMap);
        return this;
    }

    public MultiValueBuilder withValues(Object... values) {
        for (Object value : values) {
            this.values.add(getHolderForObject(value));
        }

        return this;
    }

    private ValueHolder getHolderForObject(Object object) {
        if (object instanceof BigDecimal) {
            return new NumberHolder((BigDecimal) object);
        }
        if (object instanceof Date) {
            return new DateHolder((Date) object);
        }
        if (object instanceof Number) {
            return new IntegerHolder(((Number) object).longValue());
        }
        if (object == null) {
            return new StringHolder(null);
        }
        if (object instanceof Enum<?>) {
            return new StringHolder(((Enum<?>) object).name());
        }
        return new StringHolder(object.toString());
    }
}
