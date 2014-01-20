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
package org.smartparam.engine.core.output;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.smartparam.engine.core.repository.RepositoryName;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class DefaultParamValue extends AbstractParamValue {

    private final List<MultiValue> rows;

    public DefaultParamValue(List<MultiValue> rows, RepositoryName sourceRepository) {
        super(sourceRepository);
        this.rows = rows;
    }

    public static ParamValue empty() {
        return new DefaultParamValue(new ArrayList<MultiValue>(), null);
    }

    @Override
    protected MultiValue rawRowAt(int rowNo) {
        return rows.get(rowNo);
    }

    @Override
    protected List<? extends MultiValue> rawRows() {
        return rows;
    }

    @Override
    public Iterator<MultiValue> iterator() {
        return rows.iterator();
    }

}
