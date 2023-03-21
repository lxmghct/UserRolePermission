package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.UserRole;
import com.nankai.code.mapper.UserRoleMapper;
import com.nankai.code.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
