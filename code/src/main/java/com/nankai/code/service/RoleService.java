package com.nankai.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nankai.code.common.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface RoleService extends IService<Role> {

}
