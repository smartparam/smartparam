/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.report;

import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.report.tree.ReportValueChooser;
import org.smartparam.engine.report.tree.ReportingTreeValueDescriptor;

/**
 *
 * @author Adam Dubiel
 */
public abstract class DecodedValueChooser implements ReportValueChooser<PreparedEntry> {

    @SuppressWarnings("unchecked")
    protected <T> T decode(ReportingTreeValueDescriptor outputValueDescriptor, String levelName, PreparedEntry fromEntry) {
        int levelIndex = outputValueDescriptor.indexOf(levelName);
        String levelValue = fromEntry.getLevel(levelIndex);

        return (T) outputValueDescriptor.type(levelName).decode(levelValue).getValue();
    }
}
