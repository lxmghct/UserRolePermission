package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.Permission;
import com.nankai.code.common.enums.CodeEnum;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.mapper.PermissionMapper;
import com.nankai.code.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public List<String> selectComponentsByUsernameAndSystem(String username, String system) {
        return permissionMapper.selectComponentsByUsernameAndSystem(username, system);
    }

    @Override
    public ResponseVO<List<Permission>> selectAllPermissions() {
        List<Permission> permissions = null;
        try {
            permissions = permissionMapper.selectAllPermissions();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error(CodeEnum.ERROR);
        }
        return ResponseVO.success(CodeEnum.SUCCESS, permissions);
    }

    @Override
    public ResponseVO<List<Permission>> getPermissionsByRoleId(Integer roleId) {
        List<Permission> permissions = null;
        try {
            permissions = permissionMapper.getPermissionsByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error(CodeEnum.ERROR);
        }
        return ResponseVO.success(CodeEnum.SUCCESS, permissions);
    }

    @Override
    public ResponseVO roleAccessComponent(String component, String[] roleNames) {
        List<String> components = null;
        try {
            components = permissionMapper.getComponentsByRoleNames(roleNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error();
        }
        if (components != null && components.contains(component)) {
            return ResponseVO.success(CodeEnum.SUCCESS, true);
        } else {
            return ResponseVO.success(CodeEnum.SUCCESS, false);
        }
    }


}
