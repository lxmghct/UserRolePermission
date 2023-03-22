package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


    @Select("SELECT role.name FROM user_role LEFT JOIN role on role.id=user_role.role_id WHERE user_role.user_id=#{userId}")
    List<String> getRoleNameByUserId(Integer userId);


    @Select("select role_id from user_role where user_id =#{uid}")
    List<Integer> getRoleIdsByUserId(Integer uid);

    @Insert("")
    void addUserRole(Integer userId, List<Integer> roleIdList);
}
