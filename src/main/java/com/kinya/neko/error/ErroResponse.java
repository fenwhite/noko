package com.kinya.neko.error;

import com.kinya.neko.error.exception.NekoException;

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

    public ErroResponse(NekoException ex){
        this.code = ex.getCode();
        this.msg = ex.getMsg();
    }
}
