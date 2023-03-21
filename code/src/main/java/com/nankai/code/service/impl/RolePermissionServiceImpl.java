package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.RolePermission;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.mapper.RolePermissionMapper;
import com.nankai.code.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public ResponseVO updateRolePermissions(Integer roleId, List<Integer> permissionIds) {
        try {
            //1.先删除
            rolePermissionMapper.deleteRolePermissions(roleId);
            //2.再添加
            rolePermissionMapper.addRolePermissions(roleId,permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error();
        }
        return ResponseVO.success();
    }
}
