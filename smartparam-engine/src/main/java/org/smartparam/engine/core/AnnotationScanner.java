package org.smartparam.engine.core;

import java.util.List;
import org.smartparam.engine.bean.AnnotationScannerProperties;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface AnnotationScanner {

    AnnotationScannerProperties getScannerProperties();
    
    void setScannerProperties(AnnotationScannerProperties properties);
    
    void setPackagesToScan(List<String> packagePrefixes);
    
    void setScanAnnotations(boolean scanAnnotations);

}
