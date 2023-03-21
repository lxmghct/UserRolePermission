package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Delete("delete from role_permission where role_id=#{roleId}")
    void deleteRolePermissions(Integer roleId);

    @Insert("<script>" +
            "insert into role_permission(role_id,permission_id) values " +
            "<foreach collection='permissionIds' item='pid' separator=',' > (#{roleId},#{pid})</foreach>" +
            "</script>")
    void addRolePermissions(Integer roleId, List<Integer> permissionIds);
}
