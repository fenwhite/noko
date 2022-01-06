package com.kinya.neko.bean;

import com.google.gson.annotations.Expose;

public class UserBean {
    @Expose
    private String name;
    @Expose
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
