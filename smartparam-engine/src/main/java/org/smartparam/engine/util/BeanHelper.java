package org.smartparam.engine.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BeanHelper {

    public static Map<String, PropertyDescriptor> getProperties(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, clazz.getSuperclass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

        Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
        PropertyDescriptor descriptor;
        for (int descriptorIndex = 0; descriptors != null && descriptorIndex < descriptors.length; ++descriptorIndex) {
            descriptor = descriptors[descriptorIndex];
            properties.put(descriptor.getName(), descriptor);
        }

        return properties;
    }
}
