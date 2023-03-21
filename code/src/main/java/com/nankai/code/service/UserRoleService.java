package com.nankai.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nankai.code.common.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserRoleService extends IService<UserRole> {
}
