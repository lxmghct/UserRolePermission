package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select name from role where id in (select role_id from user_role where user_id=#{userId})")
    public List<String> selectRoleNameListByUserId(Integer userId);
}
