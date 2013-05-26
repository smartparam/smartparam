package org.smartparam.serializer.entries;

import org.smartparam.serializer.CsvSerializationConfig;
import org.smartparam.serializer.SerializationConfig;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public abstract class AbstractCsvParameterEntrySerializer {

    protected CsvPreference createCsvPreference(SerializationConfig config) {
        CsvSerializationConfig csvConfig = (CsvSerializationConfig) config;
        return new CsvPreference.Builder(csvConfig.getCsvQuote(), csvConfig.getCsvDelimiter(), csvConfig.getEndOfLine()).build();
    }
}
