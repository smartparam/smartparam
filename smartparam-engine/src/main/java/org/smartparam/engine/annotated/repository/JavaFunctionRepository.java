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
package org.smartparam.engine.annotated.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.smartparam.engine.annotated.annotations.ParamFunctionRepository;
import org.smartparam.engine.annotated.annotations.JavaPlugin;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.functions.java.JavaFunction;

/**
 *
 * @author Adam Dubiel
 */
@ParamFunctionRepository(JavaFunctionRepository.FUNCTION_TYPE)
public class JavaFunctionRepository extends AbstractScanningJavaFunctionRepository {

    public static final String FUNCTION_TYPE = "java";

    @Override
    protected Class<? extends Annotation> annotationClass() {
        return JavaPlugin.class;
    }

    @Override
    protected Function createFunction(String functionName, Method method) {
        return new JavaFunction(functionName, FUNCTION_TYPE, method);
    }

    @Override
    protected Class<? extends Function> functionClass() {
        return JavaFunction.class;
    }
}
