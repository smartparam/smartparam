package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.type.AbstractHolder;

/**
 * Container of parameter sub-matrix returned after querying the parameter.
 *
 * All numbers passed to methods are 1-based, meaning first entry is number 1.
 * In case of providing values that are out of bounds,
 * {@link org.smartparam.engine.core.exception.SmartParamException} with
 * {@link org.smartparam.engine.core.exception.SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}
 * reason is thrown.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamValue {

    /**
     * Return resulting matrix row.
     *
     * @param rowNo row number, 1-based
     * @return row representation
     */
    MultiValue row(int rowNo);

    /**
     * Returns first row of matrix.
     *
     * @return
     */
    MultiValue row();

    /**
     * Return all rows of matrix.
     *
     * @return
     */
    MultiValue[] rows();

    /**
     * Get cell of matrix.
     *
     * @param rowNo cell row, 1-based
     * @param colNo cell column, 1-based
     * @return value held in cell
     */
    AbstractHolder get(int rowNo, int colNo);

    /**
     * Get cell of matrix using level name as column indicator.
     *
     * @param rowNo cell row, 1-based
     * @param name  name of level representing column
     * @return value held in cell
     */
    AbstractHolder get(int rowNo, String name);

    /**
     * Get value from first row and given column.
     *
     * @param colNo column number, 1-based
     * @return value held in cell
     */
    AbstractHolder get(int colNo);

    /**
     * Get value from first row using level name as column indicator.
     *
     * @param name column (level) name
     * @return value held in cell
     */
    AbstractHolder get(String name);

    /**
     * Return first value from first row, useful if parameter returns only
     * single value.
     *
     * @return value held in first cell of first row
     */
    AbstractHolder get();

    /**
     *
     * @return number of rows
     */
    int size();
}
