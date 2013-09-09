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

import org.smartparam.engine.model.Level;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterLevel implements Level {

    private long id;

    private long parameterId;

    private int orderNo;

    private String name;

    private String type;

    private String matcher;

    private String levelCreator;

    private boolean array;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParameterId() {
        return parameterId;
    }

    public void setParameterId(long parameterId) {
        this.parameterId = parameterId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JdbcParameterLevel[");
        sb.append("id=").append(id);
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
