package com.kinya.neko.controller;

import com.kinya.neko.bean.ResultWrapper;
import com.kinya.neko.bean.UserBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
public class LoginController {

    @PostMapping("api/login")
    @ResponseBody
    public ResultWrapper login(@RequestBody UserBean requestUser){
        String name = requestUser.getName();
        String pass = requestUser.getPassword();
        if(Objects.equals("miku",name) && Objects.equals("123456",pass)) {
            return new ResultWrapper(new Object());
        }else{
            return new ResultWrapper(400, "auth error", null);
        }
    }
}
