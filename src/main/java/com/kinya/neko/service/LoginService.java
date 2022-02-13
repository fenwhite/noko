package com.kinya.neko.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kinya.neko.bean.UserBean;
import com.kinya.neko.mapper.LoginMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：white
 * @description：登录相关
 * @date ：2022/1/16
 */

@Service
public class LoginService extends NekoService{
    @Autowired
    LoginMapper loginMapper;

    public boolean login(UserBean loginUser){
        QueryWrapper<UserBean> cond = new QueryWrapper<>();
        cond.eq("name", loginUser.getName());
        // return userbead with user password
        UserBean pas = loginMapper.selectOne(cond);
        return StringUtils.equals(loginUser.getPassword(), pas.getPassword());
    }

}
