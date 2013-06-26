package org.smartparam.engine.util;

import java.beans.PropertyDescriptor;
import java.util.Map;
import org.junit.Test;
import org.smartparam.engine.test.bean.DummyBean;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BeanHelperIntegrationTest {

    @Test
    public void shouldReturnAllPropertiesOfBean() throws Exception {
        // given

        // when
        Map<String, PropertyDescriptor> properties = BeanHelper.getProperties(DummyBean.class);

        // then
        assertThat(properties.keySet()).containsOnly("propertyOne", "propertyTwo", "propertyThree");
    }
}
