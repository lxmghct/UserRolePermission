package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select id,username,password,truename,avatar,status,phone,address,note,create_time,sex from user where username=#{username}")
    public User getUserByUsername(String username);

    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "truename", property = "truename"),
            @Result(column = "password", property = "password"),
            @Result(column = "sex", property = "sex"),
            @Result(column = "avatar", property = "avatar"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "address", property = "address"),
            @Result(column = "note", property = "note"),
            @Result(column = "id", property = "roleNameList", many = @Many(select = "com.nankai.code.mapper.RoleMapper.selectRoleNameListByUserId", fetchType = FetchType.DEFAULT))
    })
    @Select("select * from user order by create_time desc limit #{startNum},#{pageSize}")
    public List<User> searchUserList(Integer startNum, Integer pageSize);

}
