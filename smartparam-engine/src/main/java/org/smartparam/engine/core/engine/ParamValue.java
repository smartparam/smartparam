package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamValue {

	MultiValue row(int rowNo);

	MultiValue row();

	MultiValue[] rows();

	AbstractHolder get(int rowNo, int colNo);

	AbstractHolder get(int rowNo, String name);

	AbstractHolder get(int colNo);

	AbstractHolder get(String name);

	AbstractHolder get();

	int size();
}
