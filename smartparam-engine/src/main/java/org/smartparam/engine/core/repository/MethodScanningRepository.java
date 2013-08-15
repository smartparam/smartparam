package org.smartparam.engine.core.repository;

import org.smartparam.engine.annotations.scanner.MethodScanner;

/**
 *
 * @author Adam Dubiel
 */
public interface MethodScanningRepository {

    void scanMethods(MethodScanner scanner);

}
