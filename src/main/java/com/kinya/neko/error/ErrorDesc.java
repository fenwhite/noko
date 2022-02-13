package com.kinya.neko.error;

/**
 * @author ：white
 * @description：错误码及其描述
 * @date ：2022/1/15
 */

public enum ErrorDesc {
    NOT_IMPL(100, "功能未实现"),
    PARAM_EMPTY(101,"传入参数为空");

    private final int code;
    private final String desc;

    ErrorDesc(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
