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
package org.smartparam.repository.fs.resolver;

import java.io.Reader;
import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.repository.fs.util.StreamReaderOpener;
import org.smartparam.serializer.entries.BatchReaderWrapper;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BatchClasspathReaderWrapper implements BatchReaderWrapper {

    private Class<?> classloaderClass;

    private String resourceName;

    public BatchClasspathReaderWrapper(Class<?> classloaderClass, String resourceName) {
        this.classloaderClass = classloaderClass;
        this.resourceName = resourceName;
    }

    @Override
    public Reader initializeReader() throws ParamBatchLoadingException {
        return StreamReaderOpener.openReaderForResource(classloaderClass, resourceName);
    }

}
