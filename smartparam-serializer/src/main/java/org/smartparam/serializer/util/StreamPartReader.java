/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.serializer.util;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author Adam Dubiel
 */
public final class StreamPartReader {

    private static final int PART_BUFFER_SIZE = 100;

    private StreamPartReader() {
    }

    public static String readPart(Reader reader, char openingChar, char closingChar) throws IOException {
        StringBuilder part = new StringBuilder(PART_BUFFER_SIZE);

        int characterCode = reader.read();
        int pairCounter = 0;
        char character;
        boolean read = false;
        while (characterCode != -1) {
            character = (char) characterCode;
            if (character == openingChar) {
                read = true;
                pairCounter++;
            }

            if (read) {
                part.append(character);
            }

            if (character == closingChar) {
                pairCounter--;
                if (pairCounter == 0) {
                    break;
                }
            }

            characterCode = reader.read();
        }

        if (pairCounter != 0) {
            throw new IllegalStateException("Stream ended but no ending char found! Possibly because of dangling opening char.");
        }

        return part.toString();
    }
}
