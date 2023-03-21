package com.nankai.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nankai.code.common.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserService extends IService<User> {
}
