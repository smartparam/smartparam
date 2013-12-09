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
package org.smartparam.repository.fs;

import org.smartparam.repository.fs.resolver.ResourceResolver;
import org.smartparam.repository.fs.resolver.ClasspathResourceResolver;
import org.smartparam.serializer.ParamDeserializer;

/**
 *
 * @author Adam Dubiel
 */
public class ClasspathParamRepository extends AbstractFSParamRepository {

    public ClasspathParamRepository(String basePath, String filePattern) {
        super(basePath, filePattern);
    }

    public ClasspathParamRepository(String basePath, String filePattern, ParamDeserializer deserializer) {
        super(basePath, filePattern, deserializer);
    }

    @Override
    protected ResourceResolver createResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
        return new ClasspathResourceResolver(basePath, filePattern, deserializer);
    }

}
