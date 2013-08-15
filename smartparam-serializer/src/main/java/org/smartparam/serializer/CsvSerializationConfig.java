package org.smartparam.serializer;

/**
 *
 * @author Adam Dubiel
 */
public interface CsvSerializationConfig extends SerializationConfig {

    char getCsvDelimiter();

    char getCsvQuote();
}
