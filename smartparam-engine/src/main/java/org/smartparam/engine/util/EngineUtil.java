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
package org.smartparam.engine.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class with common String operations optimized for ParamEngine.
 *
 * During ParamEngine performance tuning we decided, that writing own, single-purpose implementations of
 * some commons methods can significantly speed up parameter evaluation.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public final class EngineUtil {

    private EngineUtil() {
    }

    public static boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Split string using single-char separator into maximum of N tokens. This method stops after splitting string
     * into maximum number of tokens, even if it could be split further. 4-times quicker than native String.split()
     * method when called with single-char separator.
     *
     * @param str input string
     * @param delim separator
     * @param maxTokens maximum number of tokens, 0 for unlimited
     * @return token array or empty array, never null
     */
    public static String[] split(final String str, final char delim, final int maxTokens) {
        int max = maxTokens;
        List<String> result = new ArrayList<String>(max);
        if (max == 0) {
            max = -1;
        }

        int curr = -1;
        int prev = 0;
        while (true) {
            ++curr;
            if (curr == str.length()) {
                result.add(str.substring(prev, str.length()));
                --max;
                break;
            }
            if (str.charAt(curr) == delim) {
                result.add(str.substring(prev, curr));
                --max;
                prev = curr + 1;
            }

            if (max == 0) {
                break;
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Shorthand for {@link #split(java.lang.String, char, int) }, splits string into unlimited number of tokens.
     */
    public static String[] split(final String str, final char delim) {
        return split(str, delim, 0);
    }

    /**
     * Special case implementation of split that always splits given String into 2-element array using given separator.
     * This method always returns 2-element array, if string could not be split second element is empty string. If passed
     * string is null, both elements in resulting array are empty strings.
     *
     * It is 6 time faster than {@link #split(java.lang.String, char, int) } and ~24 times faster than native
     * {@link String#split(java.lang.String) }.
     *
     */
    public static String[] split2(final String str, final char delim) {
        String[] result = {"", ""};
        if (str != null) {
            int ix = str.indexOf(delim);
            if (ix >= 0) {
                result[0] = str.substring(0, ix);
                result[1] = str.substring(ix + 1);
            } else {
                result[0] = str;
            }
        }
        return result;
    }

    public static String trimAllWhitespace(final String str) {
        if (!hasText(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }
}
