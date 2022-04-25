package com.kinya.neko.controller;

import com.kinya.neko.bean.ResultWrapper;
import com.kinya.neko.bean.UserBean;
import com.kinya.neko.error.ErrorDesc;
import com.kinya.neko.error.exception.NekoException;
import com.kinya.neko.service.LoginService;
import com.kinya.neko.utils.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends NekoController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    @ResponseBody
    public ResultWrapper login(@RequestBody UserBean requestUser){
        String pass = StringUtils.trimToEmpty(requestUser.getPassword());
        String name = StringUtils.trimToEmpty(requestUser.getName());
        if(StringUtils.isEmpty(pass) || StringUtils.isEmpty(name)){
            throw new NekoException(ErrorDesc.PARAM_EMPTY);
        }

        String plaintText = EncryptUtil.decryptWithRSA(pass);

        requestUser.setName(name);
        requestUser.setPassword(plaintText);

        if(loginService.login(requestUser)) {
            return new ResultWrapper(new Object());
        }else{
            throw new NekoException(ErrorDesc.NOT_IMPL);
        }
    }
}
