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
package org.smartparam.repository.jdbc.schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SchemaLookupResult {

    private List<String> existingEntities = new ArrayList<String>();

    private List<String> missingEntities = new ArrayList<String>();

    public void addExistingEntity(String existingEnitty) {
        existingEntities.add(existingEnitty);
    }

    public void addMissingEntity(String missingEnitty) {
        missingEntities.add(missingEnitty);
    }

    public void addEntity(String entityName, boolean exists) {
        if(exists) {
            addExistingEntity(entityName);
        }
        else {
            addMissingEntity(entityName);
        }
    }

    public boolean noEntityMissing() {
        return missingEntities.isEmpty();
    }

    public boolean noEntityExisting() {
        return existingEntities.isEmpty();
    }

    public List<String> getExistingEntities() {
        return existingEntities;
    }

    public List<String> getMissingEntities() {
        return missingEntities;
    }

}
