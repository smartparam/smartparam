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
package org.smartparam.repository.jdbc.core.query;

import java.util.List;
import java.util.Set;
import org.smartparam.repository.jdbc.core.mapper.ObjectMapper;
import org.smartparam.repository.jdbc.core.transaction.Transaction;

/**
 *
 * @author Adam Dubiel
 */
public interface QueryRunner {

    <T> List<T> queryForList(Query query, ObjectMapper<T> mapper);

    <T> Set<T> queryForSet(Query query, ObjectMapper<T> mapper);

    <T> T queryForObject(Query query, ObjectMapper<T> mapper);

    boolean queryForExistence(Query query);

    void execute(Transaction transaction, Query... queries);

    void execute(Query... queries);
}
