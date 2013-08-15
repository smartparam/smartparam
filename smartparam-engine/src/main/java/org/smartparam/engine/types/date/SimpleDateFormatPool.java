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
package org.smartparam.engine.types.date;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class SimpleDateFormatPool {

    private static ThreadLocal<Map<String, SimpleDateFormat>> pool = new ThreadLocal<Map<String, SimpleDateFormat>>() {

        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<String, SimpleDateFormat>();
        }
    };

    //boost 10x (jest szybsze dokladnie 10x w stosunku do tworzenia new SimpleDateFormat)
    public static SimpleDateFormat get(String pattern) {

        Map<String, SimpleDateFormat> map = pool.get();

        SimpleDateFormat sdf = map.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            map.put(pattern, sdf);
        }
        return sdf;
    }
}
