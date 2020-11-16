package com.flance.components.fastdfs.infrastructure.fastdfs;

/**
 * 数据源配置
 */
public enum SingleTracker {

    RELEASE(false),
    SINGLE(true);

    private boolean value;

    SingleTracker(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

}
