package org.smartparam.demo.param.type;

import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.core.type.AbstractType;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
@SmartParamType("demoType")
public class DemoType extends AbstractType<DemoTypeHolder> {

    @Override
    public String encode(DemoTypeHolder holder) {
        return holder.getString();
    }

    @Override
    public DemoTypeHolder decode(String text) {
        return new DemoTypeHolder(text);
    }

    @Override
    public DemoTypeHolder convert(Object obj) {
        if (obj != null) {
            return new DemoTypeHolder(obj.toString());
        }
        return new DemoTypeHolder(null);
    }

    @Override
    public DemoTypeHolder[] newArray(int size) {
        return new DemoTypeHolder[size];
    }
}
