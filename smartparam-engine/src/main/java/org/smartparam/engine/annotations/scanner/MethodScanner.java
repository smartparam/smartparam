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
package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Annotation scanner util specializing in scanning methods.
 *
 * @author Adam Dubiel
 */
public interface MethodScanner {

    /**
     * Return all methods annotated with given annotation that can be found in
     * packages. Annotation should have a value() method returning string, as
     * its value will be used as method unique name. If more than one method
     * has same name, {@link SmartParamException} is thrown.
     *
     * @param annotationClass annotation to look for
     *
     * @return map (name -> method) of methods (no ordering guaranteed)
     */
    Map<String, Method> scanMethods(Class<? extends Annotation> annotationClass);
}
