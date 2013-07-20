package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ParamValueImpl implements ParamValue {

	private MultiValue[] rows;

	private Map<String, Integer> indexMap;

	public ParamValueImpl(MultiValue[] rows, Map<String, Integer> indexMap) {
		this.rows = rows;
		this.indexMap = indexMap;
	}

	@Override
	public MultiValue row(int rowNo) {
		if (rowNo >= 1 && rowNo <= size()) {
			return rows[rowNo - 1];
		}

		throw new SmartParamUsageException(
				SmartParamErrorCode.INDEX_OUT_OF_BOUNDS,
				"Getting element from non-existing position: " + rowNo);
	}

	@Override
	public MultiValue row() {
		return row(1);
	}

	@Override
	public MultiValue[] rows() {
		return rows;
	}

	@Override
	public AbstractHolder get(int rowNo, int colNo) {
		return row(rowNo).getValue(colNo);
	}

	@Override
	public AbstractHolder get(int rowNo, String name) {
		return get(rowNo, index(name));
	}

	@Override
	public AbstractHolder get(int colNo) {
		return row().getValue(colNo);
	}

	@Override
	public AbstractHolder get(String name) {
		return row().getValue(index(name));
	}

	@Override
	public AbstractHolder get() {
		return row().getValue(1);
	}

	@Override
	public int size() {
		return rows != null ? rows.length : 0;
	}

	private int index(String name) {
		if (indexMap != null) {
			Integer k = indexMap.get(name);
			if (k != null) {
				return k;
			}
		}

		throw new SmartParamException("Unknown level name: " + name);
	}

	@Override
	public String toString() {
		String header = "ParamValue " + indexMap;
		return Printer.print(Arrays.asList(rows), header, 0, new MultiValueInlineFormatter());
	}

	static final class MultiValueInlineFormatter implements Formatter {

		@Override
		public String format(Object obj) {
			MultiValue mv = (MultiValue) obj;
			return mv.toStringInline();
		}
	}

}
