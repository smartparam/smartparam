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
package org.smartparam.repository.jdbc.schema.loader;

import org.smartparam.repository.jdbc.schema.loader.ClasspathSchemaDefinitionLoader;
import org.smartparam.repository.jdbc.dialect.Dialect;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathSchemaDefinitionLoaderTest {

    private ClasspathSchemaDefinitionLoader classpathSchemaDefinitionLoader;

    @BeforeMethod
    public void setUp() {
        classpathSchemaDefinitionLoader = new ClasspathSchemaDefinitionLoader("/ddl/", ":dialect_ddl.sql", "\\:dialect");
    }

    @Test
    public void shouldReadContentsOfDialectFilePerservingLinebreaks() {
        // given
        // when
        String ddlQuery = classpathSchemaDefinitionLoader.getQuery(Dialect.ORACLE);

        // then
        assertThat(ddlQuery).startsWith("-- parameter\n"
                + "CREATE TABLE :parameterTableName (");
    }

    @Test
    public void shouldBailIfDDLFileDoesNotExist() {
        // given
        // when
        catchException(classpathSchemaDefinitionLoader).getQuery(Dialect.POSTGRESQL);

        // then
        assertThat(caughtException()).isNotNull().isInstanceOf(SmartParamJdbcException.class);
    }
}