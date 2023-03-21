package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select id,user_name as userName,password,sublibrary_id,true_name as trueName,avatar,status,remaning,phone,address,note,login_time,create_time,sex from user where user_name=#{username}")
    public User getUserByUsername(String username);

}
