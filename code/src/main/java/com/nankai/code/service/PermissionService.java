package com.nankai.code.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nankai.code.common.Permission;
import com.nankai.code.common.vo.ResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public interface PermissionService extends IService<Permission> {
    List<String> selectComponentsByUsernameAndSystem(String username, String system);

    ResponseVO<List<Permission>> selectAllPermissions();

    ResponseVO<List<Permission>> getPermissionsByRoleId(Integer roleId);

    ResponseVO roleAccessComponent(String component, String[] roleNames);
}
