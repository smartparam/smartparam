
package org.smartparam.serializer;

import java.nio.charset.Charset;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface SerializationConfig {

    char getCommentChar();

    String getEndOfLine();

    Charset getCharset();

}
