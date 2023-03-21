package com.nankai.code.security.service;

import com.nankai.code.common.User;
import com.nankai.code.mapper.PermissionMapper;
import com.nankai.code.mapper.UserMapper;
import com.nankai.code.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询数据库，查看用户是否存在
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            //1.1抛出异常，异常会被过滤器链捕获
            throw new UsernameNotFoundException("用户不存在");
        }
        //2.根据用户id查询角色
        List<Integer> roleIds = userRoleMapper.getRoleIdsByUserId(user.getId());
        //3.根据用户id查询角色名称
        List<String> roleNames = userRoleMapper.getRoleNameByUserId(user.getId());
        //4.根据角色id查询权限
        Set<String> permissionCodes = new HashSet<>();
        for (Integer rid : roleIds) {
            List<String> perms = permissionMapper.selectPermissionsByRoleId(rid);
            permissionCodes.addAll(perms);
        }
        //5.设置权限和角色
        user.setPermissions(permissionCodes);
        user.setRoleNameList(roleNames);
        return user;
    }
}
