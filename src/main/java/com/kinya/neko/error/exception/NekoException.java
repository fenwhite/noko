package com.kinya.neko.error.exception;

import com.kinya.neko.error.ErrorDesc;

/**
 * @author ：white
 * @description：异常基类
 * @date ：2022/1/15
 */

public class NekoException extends RuntimeException{
    private int code;
    private String msg;

    public NekoException(ErrorDesc desc){
        super(desc.getDesc());
        this.code = desc.getCode();
        this.msg = desc.getDesc();
    }
    public NekoException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
