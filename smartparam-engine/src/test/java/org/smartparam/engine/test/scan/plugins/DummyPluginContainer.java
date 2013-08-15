package org.smartparam.engine.test.scan.plugins;

import org.smartparam.engine.annotations.JavaPlugin;

/**
 *
 * @author Adam Dubiel
 */
public class DummyPluginContainer {

    @JavaPlugin("javaPlugin")
    public void javaPlugin() {
    }

    @DummyPluginAnnotation("dummyPlugin")
    public void dummyPlugin() {
    }

    @DummyPluginAnnotation("dummyPlugin")
    public void dummyPluginDuplicate() {
    }

    @DummyPluginAnnotationWithoutValue
    public void dummyPluginForAnnotationWithoutValue() {
    }
}
