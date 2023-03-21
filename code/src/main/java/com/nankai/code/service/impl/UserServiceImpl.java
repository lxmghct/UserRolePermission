package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.User;
import com.nankai.code.mapper.UserMapper;
import com.nankai.code.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
