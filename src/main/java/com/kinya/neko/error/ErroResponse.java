package com.kinya.neko.error;

/**
 * @author ：white
 * @description：统一异常返回响应体
 * @date ：2022/1/14
 */

public class ErroResponse {
    private int code;
    private String msg;

    public ErroResponse(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public ErroResponse(ErrorDesc desc){
        this.code = desc.getCode();
        this.msg = desc.getDesc();
    }
}
