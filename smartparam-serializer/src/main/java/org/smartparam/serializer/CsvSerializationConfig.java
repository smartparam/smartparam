package org.smartparam.serializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface CsvSerializationConfig extends SerializationConfig {

    char getCsvDelimiter();

    char getCsvQuote();
}
