package org.smartparam.serializer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSerializationConfig implements CsvSerializationConfig {

    private static final String DEFAULT_END_OF_LINE = "\n";

    private static final char DEFAULT_CSV_DELIMITER = ';';

    private static final char DEFAULT_CSV_QUOTE = '"';

    private static final char DEFAULT_COMMENT = '#';

    private char csvQuote;

    private char csvDelimiter;

    private char comment;

    private String endOfLine;

    public StandardSerializationConfig() {
        this(DEFAULT_CSV_QUOTE, DEFAULT_CSV_DELIMITER, DEFAULT_COMMENT, DEFAULT_END_OF_LINE);
    }

    public StandardSerializationConfig(char csvQuote, char csvDelimiter, char comment, String endOfLine) {
        this.csvQuote = csvQuote;
        this.csvDelimiter = csvDelimiter;
        this.comment = comment;
        this.endOfLine = endOfLine;
    }

    @Override
    public char getCsvDelimiter() {
        return csvDelimiter;
    }

    @Override
    public char getCsvQuote() {
        return csvQuote;
    }

    @Override
    public String getEndOfLine() {
        return endOfLine;
    }

    @Override
    public char getCommentChar() {
        return comment;
    }
}
