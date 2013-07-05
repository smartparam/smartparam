package org.smartparam.engine.core.repository;

import org.smartparam.engine.annotations.scanner.MethodScanner;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface MethodScanningRepository {

    void scanMethods(MethodScanner scanner);

}
