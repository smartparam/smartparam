package org.smartparam.engine.util;

import java.beans.PropertyDescriptor;
import java.util.Map;
import org.junit.Test;
import org.smartparam.engine.test.beans.DummyBean;
import static org.fest.assertions.Assertions.*;
import org.smartparam.engine.test.beans.DummyEmptyBean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BeanHelperTest {

    @Test
    public void testGetProperties() throws Exception {
        Map<String, PropertyDescriptor> properties = BeanHelper.getProperties(DummyBean.class);

        assertThat(properties).hasSize(3);
        assertThat(properties.keySet()).containsOnly("propertyOne", "propertyTwo", "propertyThree");
    }

    @Test
    public void testGetProperties_emptyBean() throws Exception {
        Map<String, PropertyDescriptor> properties = BeanHelper.getProperties(DummyEmptyBean.class);

        assertThat(properties).isEmpty();
    }
}
