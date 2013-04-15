package org.smartparam.engine.core.function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public enum FunctionRepositoryCapabilities {

    ALL(true, true),
    BATCH(false, true),
    SINGLE(true, false);

    private boolean supportsSingle;

    private boolean supportsBatch;

    private FunctionRepositoryCapabilities(boolean supportsSingle, boolean supportsBatch) {
        this.supportsSingle = supportsSingle;
        this.supportsBatch = supportsBatch;
    }

    public boolean isSupportsBatch() {
        return supportsBatch;
    }

    public boolean isSupportsSingle() {
        return supportsSingle;
    }
}
