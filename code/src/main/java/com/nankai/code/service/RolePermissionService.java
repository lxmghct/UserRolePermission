package com.nankai.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nankai.code.common.RolePermission;
import com.nankai.code.common.vo.ResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface RolePermissionService extends IService<RolePermission> {
    ResponseVO updateRolePermissions(Integer roleId, List<Integer> permissionIds);
}
