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
package org.smartparam.transferer.operation;

/**
 * Specifies operations done by transferer. Depending on setup it can:
 * <ul>
 * <li>override parameters that exist in both repositories (multiplication of sets)</li>
 * <li>create parameters that don't exist in target repository (sum of sets)</li>
 * <li>delete parameters that don't exist in source but exist in target (difference of sets)</li>
 * </ul>
 *
 * To perform  clone of repository specify all operations.
 *
 * @author Adam Dubiel
 */
public enum TransferOperationType {

    /**
     * Override parameter in target repository.
     */
    OVERRIDE,
    /**
     * Create parameter in target repository if it does not exist.
     */
    CREATE,
    /**
     * Delete parameter in target repository if it does not exist in source repo.
     */
    DELETE;

}
