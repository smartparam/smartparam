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

import org.smartparam.engine.model.IdentifiableEntity;
import org.smartparam.engine.model.editable.EditableLevel;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcLevel implements EditableLevel, IdentifiableEntity {

    private final JdbcEntityKey key;

    private int orderNo;

    private String name;

    private String type;

    private String matcher;

    private String levelCreator;

    private boolean array;

    public JdbcLevel(long id, String parameterName) {
        this.key = new JdbcEntityKey(id, parameterName);
    }

    @Override
    public JdbcEntityKey getEntityKey() {
        return key;
    }

    public long getId() {
        return key.getId();
    }

    public int getOrderNo() {
        return orderNo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevelCreator() {
        return levelCreator;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public String getMatcher() {
        return matcher;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    @Override
    public void setArray(boolean array) {
        this.array = array;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JdbcParameterLevel[");
        sb.append("id=").append(getId());
        sb.append(", orderNo=").append(orderNo);
        sb.append(", type=").append(type);

        if (matcher != null) {
            sb.append(", matcher=").append(matcher);
        }

        if (array) {
            sb.append(" array");
        }

        sb.append(", label=").append(name);

        sb.append(']');
        return sb.toString();
    }
}
