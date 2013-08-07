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
package org.smartparam.repository.fs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StreamReaderOpener {

    public static BufferedReader openReaderForFile(String filePath, Charset charset) {
        try {
            File file = new File(filePath);
            return Files.newBufferedReader(file.toPath(), charset);
        } catch (IOException exception) {
            throw new SmartParamResourceResolverException("unable to open reader stream to file " + filePath, exception);
        }
    }

    public static BufferedReader openReaderForResource(Class<?> classloaderClass, String classpathResourceName) {
        InputStream stream = classloaderClass.getResourceAsStream(classpathResourceName);
        if (stream == null) {
            throw new SmartParamResourceResolverException("no resource " + classpathResourceName + " found in classpath");
        }
        return new BufferedReader(new InputStreamReader(stream));
    }
}
