package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.User;
import com.nankai.code.mapper.UserMapper;
import com.nankai.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> searchUserList(Integer startNum, Integer pageSize) {
        return userMapper.searchUserList(startNum, pageSize);
    }
}
