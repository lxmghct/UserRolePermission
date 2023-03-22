package com.nankai.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nankai.code.common.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select permission_code from permission where id in (select permission_id from role_permission where role_id=#{rid})")
    public List<String> selectPermissionsByRoleId(Integer rid);


    @Select("select p.component from permission as p " +
            "left join role_permission as rp on p.id=rp.permission_id " +
            "left join user_role as ur on rp.role_id=ur.role_id " +
            "left join user as u on ur.user_id=u.id " +
            "where u.username=#{username} and p.classify='页面权限' and p.system=#{system}")
    public List<String> selectComponentsByUsernameAndSystem(String username, String system);

    @Select("select * from permission order by `system`,classify")
    public List<Permission> selectAllPermissions();

    @Select("select * from permission where id in (select permission_id from role_permission where role_id=#{roleId}) order by system,classify;")
    List<Permission> getPermissionsByRoleId(Integer roleId);

    @Select("<script>" +
            "select p.component from permission as p where id in (" +
            "select rp.permission_id from role_permission as rp " +
            "left join role as r " +
            "on rp.role_id=r.id " +
            "where r.name in " +
            "<foreach collection='array' open='(' item='roleName' separator=',' close=')'> #{roleName}</foreach> " +
            ")" +
            "</script>")
    List<String> getComponentsByRoleNames(String[] roleNames);
}
