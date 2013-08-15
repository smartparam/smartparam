
package org.smartparam.serializer;

import java.nio.charset.Charset;

/**
 *
 * @author Adam Dubiel
 */
public interface SerializationConfig {

    char getCommentChar();

    String getEndOfLine();

    Charset getCharset();

}
