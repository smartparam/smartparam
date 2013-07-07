package org.smartparam.engine.core.repository;

import org.smartparam.engine.annotations.scanner.TypeScanner;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface TypeScanningRepository {

    void scanAnnotations(TypeScanner scanner);
}
