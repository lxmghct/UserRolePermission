package com.nankai.code.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nankai.code.common.Permission;
import com.nankai.code.common.Role;
import com.nankai.code.common.UserRole;
import com.nankai.code.common.enums.CodeEnum;
import com.nankai.code.common.utils.TokenUtil;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.service.PermissionService;
import com.nankai.code.service.RolePermissionService;
import com.nankai.code.service.RoleService;
import com.nankai.code.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 添加新角色
     *
     * @param name
     * @param description
     * @return
     */
    @PostMapping("/addRole")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO<Map> addRole(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "description") String description) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        //1.根据角色名称获取已有角色
        Role role = roleService.getOne(queryWrapper);
        //1.1若存在同名角色
        if (role != null) {
            return ResponseVO.error(CodeEnum.ERROR, "该角色已存在！");
        } else {
            //1.2不存在同名角色
            role = new Role();
            role.setName(name);
            role.setDescription(description);
            role.setCreateTime(new Date());
            //2.添加新角色
            roleService.save(role);
        }
        return ResponseVO.success(CodeEnum.SUCCESS);

    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteRole")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO<Map> deleteRole(@RequestParam(value = "id") int id) {
        Role role = roleService.getById(id);
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        //1.检查当前角色是否正在被使用
        if (userRoleService.count(wrapper) != 0) {
            //1.1正在被使用
            return ResponseVO.error(CodeEnum.ERROR, "此角色在使用中");
        } else {
            //1.2未被使用
            if (roleService.removeById(id)) {
                return ResponseVO.success(CodeEnum.SUCCESS);
            }
        }
        return ResponseVO.error(CodeEnum.ERROR, "删除失败");
    }

    /**
     * 获取所有角色
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/getRoleList")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO<Map> getRoleList(@RequestParam(value = "pageNum", required = true) int pageNum,
                                       @RequestParam(value = "pageSize", required = true) int pageSize) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        //按照创建时间降序排序
        wrapper.orderByDesc("create_time");
        //1.获取所有角色的数量
        long total = roleService.count(wrapper);
        //2.limit分页查询角色
        int startNum = (pageNum - 1) * pageSize;
        wrapper.last("limit" + " " + startNum + "," + pageSize);
        List<Role> list = roleService.list(wrapper);
        //3.封装返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("roleList", list);
        roleMap.put("total", total);

        return ResponseVO.success(CodeEnum.SUCCESS, roleMap);
    }

    /**
     * 根据角色编号获取权限
     *
     * @param roleId
     * @return
     */
    @PostMapping("/getPermissionsByRoleId")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO<List<Permission>> getPermissionsByRoleId(@RequestParam("roleId") Integer roleId) {
        if (roleId == null)
            return ResponseVO.error(CodeEnum.ERROR, "输入角色编号不能为空");
        return permissionService.getPermissionsByRoleId(roleId);
    }

    /**
     * 更新角色的权限
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    @PutMapping("/updateRolePermissions")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO updateRolePermissions(@RequestParam("roleId") Integer roleId,
                                            @RequestParam("permissionIds") List<Integer> permissionIds) {
        //1.前置检验
        if (roleId == null || permissionIds == null)
            return ResponseVO.error(CodeEnum.ERROR, "输入不能为空");
        if (permissionIds.size() == 0) {
            return ResponseVO.error(CodeEnum.ERROR, "没有分配任何权限");
        }
        return rolePermissionService.updateRolePermissions(roleId, permissionIds);
    }

    /**
     * 判断当前角色能否访问当前路径
     *
     * @param component
     * @param token
     * @return
     */
    @PostMapping("/roleAccessComponent")
    @PreAuthorize("hasAuthority('MAN_ROLE')")
    public ResponseVO roleAccessComponent(@RequestParam("component") String component, @RequestHeader("Authorization") String token) {
        String role = TokenUtil.getRoleFromToken(token);
        String[] roleNames = role.split(",");

        return permissionService.roleAccessComponent(component, roleNames);
    }
}
