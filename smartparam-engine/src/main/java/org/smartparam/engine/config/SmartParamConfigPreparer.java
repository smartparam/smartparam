package org.smartparam.engine.config;

import org.smartparam.engine.core.AnnotationScanner;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamConfigPreparer extends ConfigPreparer<SmartParamConfig> {

    @Override
    protected SmartParamConfig createInstance(SmartParamConfig initialConfig) {
        SmartParamConfig preparedConfig = new SmartParamConfig();
        preparedConfig.setPackagesToScan(initialConfig.getPackagesToScan());
        preparedConfig.setScanAnnotations(initialConfig.isScanAnnotations());

        return preparedConfig;
    }

    @Override
    protected void customizeNewDefaultValue(SmartParamConfig config, Object object) {
        if (object instanceof AnnotationScanner) {
            AnnotationScanner annotationScanner = (AnnotationScanner) object;
            annotationScanner.setPackagesToScan(config.getPackagesToScan());
            annotationScanner.setScanAnnotations(config.isScanAnnotations());
        }

    }
}
