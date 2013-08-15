package org.smartparam.engine.core.repository;

import org.smartparam.engine.annotations.scanner.TypeScanner;

/**
 *
 * @author Adam Dubiel
 */
public interface TypeScanningRepository {

    void scanAnnotations(TypeScanner scanner);
}
