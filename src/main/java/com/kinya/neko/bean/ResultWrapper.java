package com.kinya.neko.bean;

import com.google.gson.annotations.Expose;

public class ResultWrapper<T> {
    private int code;
    private String info;
    private T data;

    public ResultWrapper(int code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public ResultWrapper(T data) {
        this.code = 200;
        this.info = "success";
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
