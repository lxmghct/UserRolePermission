package com.nankai.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nankai.code.common.UserRole;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.mapper.UserRoleMapper;
import com.nankai.code.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public ResponseVO addUserRole(Integer userId, List<Integer> roleIdList) {
        try {
            userRoleMapper.addUserRole(userId,roleIdList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error();
        }
        return ResponseVO.success();
    }
}
