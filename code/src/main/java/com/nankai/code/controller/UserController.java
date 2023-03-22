package com.nankai.code.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nankai.code.common.User;
import com.nankai.code.common.UserRole;
import com.nankai.code.common.enums.CodeEnum;
import com.nankai.code.common.utils.TokenUtil;
import com.nankai.code.common.vo.ResponseVO;
import com.nankai.code.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PermissionService permissionService;

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @param system
     * @return
     */
    @PostMapping("/login")
    public ResponseVO<Map> login(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "system") String system) {
        //1.登录前置校验
        if (loginPreCheck(username, password)) {
            return ResponseVO.error(CodeEnum.ERROR, "用户名或密码不能为空");
        }

        //2.判断当前用户能否进入当前系统
        List<String> components = permissionService.selectComponentsByUsernameAndSystem(username, system);
        if (components == null || components.size() == 0)
            return ResponseVO.error(CodeEnum.ERROR, "用户没有权限进入当前系统");

        //3.认证
        User user = null;
        try {
            //3.1创建UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken usernameAuthentication = new UsernamePasswordAuthenticationToken(username, password);
            //3.2认证
            Authentication authentication = authenticationManager.authenticate(usernameAuthentication);
            //3.3获取对象
            user = (User) authentication.getPrincipal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof BadCredentialsException) {
                //3.4用户名或密码错误
                return ResponseVO.error(CodeEnum.ERROR, "用户名或密码错误");
            } else {
                //3.5失败
                return ResponseVO.error(CodeEnum.ERROR);
            }
        }

        //4.生成token
        String token = null;
        //4.1获取角色名列表
        List<String> roleNameList = user.getRoleNameList();
        //4.2获取权限列表
        Set<String> permissions = user.getPermissions();
        //4.3生成token
        token = TokenUtil.sign(user, StringUtils.join(roleNameList.toArray(), ","), StringUtils.join(permissions.toArray(), ","));


        //5.封装返回对象
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        map.put("component", components);

        return ResponseVO.success(CodeEnum.SUCCESS, map);
    }


    /**
     * 前置检验，判空
     *
     * @param username
     * @param password
     * @return
     */
    public boolean loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return true;
        }
        return false;
    }


    /**
     * 更新用户的角色
     *
     * @param userId
     * @param roleIdList
     * @return
     */
    @PutMapping("/updateUserRoles")
    @PreAuthorize("hasAuthority('MAN_USER')")
    public ResponseVO<Map> updateUserRoles(@RequestParam(value = "userId", required = true) Integer userId,
                                           @RequestParam(value = "roleIdList", required = true) List<Integer> roleIdList) {
        //1.前置检验
        if (roleIdList.size() == 0) {
            return ResponseVO.error(CodeEnum.ERROR, "没有分配任何角色");
        }
        //2.删除原有用户与角色的对应
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        userRoleService.remove(userRoleQueryWrapper);
        //3.添加新的用户与角色的对应
        for (int i = 0; i < roleIdList.size(); i++) {
            UserRole userRole = new UserRole();
            Date date = new Date();
            userRole.setUserId(userId)
                    .setRoleId(roleIdList.get(i))
                    .setCreateTime(date);
            userRoleService.save(userRole);
        }

        return ResponseVO.success(CodeEnum.SUCCESS, "分配角色成功");
    }


    /**
     * 查询所有用户
     *
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @PostMapping("/searchUserList")
    @PreAuthorize("hasAuthority('MAN_USER')")
    public ResponseVO<Map> searchUserList(@RequestParam(value = "pageNum", required = true) int pageNum,
                                          @RequestParam(value = "pageSize", required = true) int pageSize) throws ParseException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //1.统计用户总数
        long total = userService.count(wrapper);
        //2.获取开始页
        int startNum = (pageNum - 1) * pageSize;
        //3.查询所有用户
        List<User> userList = null;
        try {
            userList = userService.searchUserList(startNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error();
        }
        //4.封装返回值
        HashMap<String, Object> map = new HashMap<>();
        map.put("userList", userList);
        map.put("total", total);
        return ResponseVO.success(map);

    }


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAuthority('MAN_USER')")
    public ResponseVO<Integer> deleteUser(@RequestParam(value = "userId", required = true) int userId) {

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        userRoleService.remove(userRoleQueryWrapper);
        if (userService.removeById(userId)) {
            return ResponseVO.success(CodeEnum.SUCCESS);
        }
        return ResponseVO.error(CodeEnum.ERROR);
    }


    /**
     * 新增用户
     * @param username
     * @param truename
     * @param password
     * @param sex
     * @param phone
     * @param address
     * @param roleIdList
     * @return
     */
    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('MAN_USER')")
    public ResponseVO<Map> addOneUser(@RequestParam(value = "username", required = true) String username,
                                      @RequestParam(value = "truename", required = true) String truename,
                                      @RequestParam(value = "password", required = true) String password,
                                      @RequestParam(value = "sex", required = true) String sex,
                                      @RequestParam(value = "phone", required = true) String phone,
                                      @RequestParam(value = "address", required = true) String address,
                                      @RequestParam(value = "roleIdList", required = true) List<Integer> roleIdList) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        //1.查询是否存在重名用户
        User user = userService.getOne(userQueryWrapper);
        if (user != null) {
            return ResponseVO.error(CodeEnum.ERROR, "用户名已存在");
        }
        user = new User();
        user.setId(null)
                .setUsername(username)
                .setTruename(truename)
                .setSex(sex)
                .setPhone(phone)
                .setAddress(address)
                .setNote("")
                .setPassword(password)
                .setStatus(1)
                .setCreateTime(new Date())
                .setAvatar("defalut.jpg");
        //2.添加新用户
        if (userService.save(user)) {
            //3.添加用户成功后，添加用户与角色的对应关系
            int userId = userService.getOne(userQueryWrapper).getId();
            for (int i = 0; i < roleIdList.size(); i++) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId)
                        .setCreateTime(new Date())
                        .setRoleId(roleIdList.get(i));
                userRoleService.save(userRole);
            }
        }
        return ResponseVO.success(CodeEnum.SUCCESS, "用户新增成功");
    }
}
