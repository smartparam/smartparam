package org.smartparam.engine.core.config;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.smartparam.engine.annotations.AnnotatedObjectsScanner;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.annotations.handler.AnnotationHandler;
import org.smartparam.engine.annotations.handler.GenericAnnotationHandler;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.function.FunctionInvoker;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedInvokerProvider extends SmartInvokerProvider {

    private PackageList packagesToScan = new PackageList();

    @PostConstruct
    public void scan() {
        AnnotationHandler handler = new GenericAnnotationHandler(SmartParamFunctionInvoker.class);
        AnnotatedObjectsScanner scanner = new AnnotatedObjectsScanner();

        Map<String, FunctionInvoker> scannedTypes = scanner.getAnnotatedObjects(packagesToScan, handler, FunctionInvoker.class);
        for (Map.Entry<String, FunctionInvoker> typeEntry : scannedTypes.entrySet()) {
            registerInvoker(typeEntry.getKey(), typeEntry.getValue());
        }
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.setPackages(packagesToScan);
    }

}
