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

import java.util.concurrent.Executors;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class QueuingParameterRequestResolverTest {

    private QueuingParameterRequestResolver resolver;

    private final WaitingRequest request = new WaitingRequest();

    @BeforeClass
    public void initialize() {
        resolver = new QueuingParameterRequestResolver(Executors.newFixedThreadPool(2));
    }

    @Test(
            description = "Concurrent test should be run with integration tests or when sth changes in tested class", enabled = false,
            invocationCount = 10, threadPoolSize = 2
    )
    public void shouldWaitForFirstRequestToResolveBeforeCallingAnotherForSameParameter() {
        // when
        resolver.resolve("test", request);

        // then
        assertThat(request.invocations).isLessThanOrEqualTo(2);
    }

    private static class WaitingRequest implements ParameterRequest {

        int invocations = 0;

        @Override
        public PreparedParameter loadAndPrepare(String parameterName) {
            synchronized (this) {
                try {
                    invocations++;
                    this.wait(100);
                } catch (InterruptedException ex) {
                    // ignore please
                }
            }
            return null;
        }

    }
}
