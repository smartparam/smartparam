/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.parameter.request;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class QueuingParameterRequestResolver implements ParameterRequestQueue {

    private final ExecutorService executorService;

    private final ConcurrentHashMap<String, Future<PreparedParameter>> currentRequests = new ConcurrentHashMap<String, Future<PreparedParameter>>();

    public QueuingParameterRequestResolver(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public PreparedParameter resolve(String parameterName, ParameterRequest request) {
        Future<PreparedParameter> waitingRequest = currentRequests.get(parameterName);
        if (waitingRequest == null) {
            waitingRequest = executorService.submit(new ParameterResolvingCallable(parameterName, request));
            currentRequests.put(parameterName, waitingRequest);
        }
        return resolvePromise(parameterName, waitingRequest);
    }

    private PreparedParameter resolvePromise(String parameterName, Future<PreparedParameter> promise) {
        try {
            return promise.get();
        } catch (InterruptedException interruptedException) {
            throw new ParameterRequestException(parameterName, interruptedException);
        } catch (ExecutionException executionException) {
            throw new ParameterRequestException(parameterName, executionException);
        }
    }

    private static class ParameterResolvingCallable implements Callable<PreparedParameter> {

        private final String parameterName;

        private final ParameterRequest request;

        ParameterResolvingCallable(String parameterName, ParameterRequest request) {
            this.parameterName = parameterName;
            this.request = request;
        }

        @Override
        public PreparedParameter call() throws Exception {
            return request.loadAndPrepare(parameterName);
        }

    }
}
