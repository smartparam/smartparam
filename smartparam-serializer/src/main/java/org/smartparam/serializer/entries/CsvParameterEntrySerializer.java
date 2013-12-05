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
package org.smartparam.serializer.entries;

import org.smartparam.engine.core.parameter.ParameterEntryBatchLoader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.parameter.ParamBatchLoadingException;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.util.StreamCloser;
import org.supercsv.io.CsvListWriter;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntrySerializer implements ParameterEntrySerializer {

    private static final Logger logger = LoggerFactory.getLogger(CsvParameterEntrySerializer.class);

    private static final int PARAMETER_ENTRY_BATCH_SIZE = 1000;

    @Override
    public void serialize(SerializationConfig config, Writer writer, Parameter parameter, ParameterEntryBatchLoader parameterEntryLoader) throws ParamSerializationException {
        CsvListWriter csvWriter = new CsvListWriter(writer, CsvPreferenceBuilder.csvPreference(config));

        try {
            long startTime = System.currentTimeMillis();
            logger.debug("started parameter entries serialization at {}", startTime);
            csvWriter.write(extractHeader(parameter));

            int counter = 0;
            while (parameterEntryLoader.hasMore()) {
                for (ParameterEntry entry : parameterEntryLoader.nextBatch(PARAMETER_ENTRY_BATCH_SIZE)) {
                    csvWriter.write(entry.getLevels());
                    counter++;
                    csvWriter.flush();
                }
            }

            csvWriter.flush();

            long endTime = System.currentTimeMillis();
            logger.debug("serializing {} parameter entries took {}", counter, endTime - startTime);
        } catch (IOException exception) {
            throw new EntriesCSVSerializationException(exception);
        } catch (ParamBatchLoadingException batchException) {
            throw new EntriesCSVSerializationException(batchException);
        } finally {
            StreamCloser.closeStream(writer);
        }
    }

    private List<String> extractHeader(Parameter parameter) {
        List<String> header = new ArrayList<String>(parameter.getLevels().size());
        for (Level level : parameter.getLevels()) {
            header.add(level.getName());
        }

        return header;
    }
}
