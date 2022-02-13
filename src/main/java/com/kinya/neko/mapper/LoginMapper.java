package com.kinya.neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kinya.neko.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ：white
 * @description：登录相关
 * @date ：2022/1/16
 */

@Mapper
public interface LoginMapper extends BaseMapper<UserBean> {
}
