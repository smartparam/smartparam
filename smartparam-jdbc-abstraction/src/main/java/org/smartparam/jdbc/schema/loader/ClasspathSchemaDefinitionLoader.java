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
package org.smartparam.jdbc.schema.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import org.smartparam.jdbc.dialect.Dialect;
import org.smartparam.jdbc.exception.JdbcException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathSchemaDefinitionLoader implements SchemaDefinitionLoader {

    private String classpathLocation;

    private String definitionFileFormat;

    private String dialectPlaceholder;

    public ClasspathSchemaDefinitionLoader(String classpathLocation, String definitionFileFormat, String dialectPlaceholder) {
        this.classpathLocation = classpathLocation;
        this.definitionFileFormat = definitionFileFormat;
        this.dialectPlaceholder = dialectPlaceholder;
    }

    @Override
    public String getQuery(Dialect dialect) {
        return readFileContents(dialect);
    }

    private String readFileContents(Dialect dialect) {
        String ddlFileName = createDefinitionFileName(dialect);
        InputStream stream;
        BufferedReader fileStream = null;
        try {
            stream = this.getClass().getResourceAsStream(ddlFileName);
            if (stream == null) {
                throw new IOException("resource " + ddlFileName + " not found");
            }
            fileStream = new BufferedReader(new InputStreamReader(stream));

            return readAsString(fileStream);
        } catch (IOException exception) {
            throw new JdbcException("Exception wile reading DDL file for dialect " + dialect + ","
                    + " make sure dialect is valid and DDL file" + ddlFileName + " exists.", exception);
        } finally {
            closeStream(fileStream);
        }
    }

    private void closeStream(BufferedReader stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException exception) {
            throw new JdbcException("Failed to close stream after reading schema definition!", exception);
        }
    }

    private String createDefinitionFileName(Dialect dialect) {
        return classpathLocation + definitionFileFormat.replaceFirst(dialectPlaceholder, dialect.name().toLowerCase());
    }

    private String readAsString(BufferedReader source) throws IOException {
        StringWriter target = new StringWriter();
        String line = source.readLine();
        while (line != null) {
            target.write(line + "\n");
            line = source.readLine();
        }

        return target.toString();
    }
}
