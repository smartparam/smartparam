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
package org.smartparam.engine.core.context;

import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.util.reflection.InnerReflectiveOperationException;

/**
 * When initializing dynamic context.
 *
 * @see org.smartparam.engine.core.context.DefaultContext#initialize(java.lang.Object[]).
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class ContextInitializationException extends SmartParamException {

    protected ContextInitializationException(String code, String message) {
        super(code, message);
    }

    public ContextInitializationException(InnerReflectiveOperationException exception, Object argument) {
        super("CONTEXT_INITIALIZATION_ERROR", exception,
                String.format("Unable to set argument %s on context", argument));
    }

}
