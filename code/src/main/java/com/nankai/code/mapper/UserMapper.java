package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select id,username,password,truename,avatar,status,phone,address,note,create_time,sex from user where username=#{username}")
    public User getUserByUsername(String username);

}
