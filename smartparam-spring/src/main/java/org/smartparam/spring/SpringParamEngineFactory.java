package org.smartparam.spring;

import java.util.List;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.config.MethodScannerInitializer;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.config.TypeScannerInitializer;
import org.smartparam.engine.core.engine.ParamEngine;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SpringParamEngineFactory implements FactoryBean<ParamEngine> {

    private ParamEngineConfig config;

    private boolean scanAnnotations;

    private List<String> packagesToScan;

    @Override
    public ParamEngine getObject() throws Exception {
        if(scanAnnotations) {
            injectComponentInitializers();
        }

        ParamEngineFactory factory = new ParamEngineFactory();
        return factory.createParamEngine(config);
    }

    private void injectComponentInitializers() {
        PackageList packageList = new PackageList();
        packageList.setPackages(packagesToScan);

        config.getComponentInitializers().add(new TypeScannerInitializer(packageList));
        config.getComponentInitializers().add(new MethodScannerInitializer(packageList));
    }

    @Override
    public Class<?> getObjectType() {
        return ParamEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setConfig(ParamEngineConfig config) {
        this.config = config;
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
}
