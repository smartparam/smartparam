package org.smartparam.serializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SerializationConfig {

    private char csvQuote;

    private char csvDelimiter;

    private String endOfLine;

    public SerializationConfig(char csvQuote, char csvDelimiter, String endOfLine) {
        this.csvQuote = csvQuote;
        this.csvDelimiter = csvDelimiter;
        this.endOfLine = endOfLine;
    }

    public char getCsvDelimiter() {
        return csvDelimiter;
    }

    public char getCsvQuote() {
        return csvQuote;
    }

    public String getEndOfLine() {
        return endOfLine;
    }
}
