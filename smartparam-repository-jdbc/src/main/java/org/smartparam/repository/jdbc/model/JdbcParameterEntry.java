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
package org.smartparam.repository.jdbc.model;

import java.util.Arrays;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterEntry implements ParameterEntry {

    private int id;

    private int parameterId;

    private String[] levels;

    @Override
    public String[] getLevels() {
        return levels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public void setLevels(String[] levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("JdbcParameterEntry[#").append(id);
        sb.append(' ');
        sb.append(Arrays.toString(levels));
        sb.append(']');
        return sb.toString();
    }
}
