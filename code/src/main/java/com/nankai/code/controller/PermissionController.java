package com.nankai.code.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nankai.code.common.Permission;
import com.nankai.code.common.RolePermission;
import com.nankai.code.common.enums.CodeEnum;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.service.PermissionService;
import com.nankai.code.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 新增权限
     *
     * @param name
     * @param description
     * @param classify
     * @param system
     * @param parentId
     * @return
     */
    @PostMapping("/addPermission")
    @PreAuthorize("hasAuthority('MAN_PERM')")
    public ResponseVO<Map> addPermission(@RequestParam(value = "name") String name,
                                         @RequestParam(value = "description") String description,
                                         @RequestParam(value = "classify") String classify,
                                         @RequestParam(value = "system") String system,
                                         @RequestParam(value = "parentId") Integer parentId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                .eq("classify", classify)
                .eq("system", system);
        //1.根据名称、分类、系统查询是否已有相同的权限
        Permission permission = permissionService.getOne(queryWrapper);
        if (permission != null) {
            //1.1如果已经存在
            return ResponseVO.error(CodeEnum.ERROR, "该权限已存在！");
        } else {
            //1.2如果没有存在
            permission = new Permission();
            permission.setName(name);
            permission.setDescription(description);
            permission.setSystem(system);
            permission.setParentId(parentId);
            permission.setCreateTime(new Date());
            permission.setClassify(classify);
            permissionService.save(permission);
            return ResponseVO.success(CodeEnum.SUCCESS);
        }

    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deletePermission")
    @PreAuthorize("hasAuthority('MAN_PERM')")
    public ResponseVO<Map> deletePermission(@RequestParam(value = "id") Integer id) {

        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_id", id);
        //1.查询当前权限是否正在被使用
        if (rolePermissionService.list(queryWrapper).size() == 0) {
            //1.1未被使用
            permissionService.removeById(id);
            return ResponseVO.success(CodeEnum.SUCCESS, "删除成功");
        } else {
            //1.2正在使用
            return ResponseVO.error(CodeEnum.ERROR, "该权限正在使用中");
        }
    }

    /**
     * 获取所有权限
     *
     * @return
     */
    @GetMapping("/getAllPermissions")
    @PreAuthorize("hasAuthority('MAN_PERM')")
    public ResponseVO<List<Permission>> getAllPermissions() {
        return permissionService.selectAllPermissions();
    }

}
