package com.kinya.neko.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kinya.neko.bean.UserBean;
import com.kinya.neko.mapper.LoginMapper;
import com.kinya.neko.utils.EncryptUtil;
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
        UserBean cipherInfo = loginMapper.selectOne(cond);

        String password = EncryptUtil.decryptWithAES(cipherInfo.getPassword(), cipherInfo.getSalt());

        return StringUtils.equals(loginUser.getPassword(), password);
    }

}
