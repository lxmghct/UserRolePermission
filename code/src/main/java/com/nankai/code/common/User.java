package com.nankai.code.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String truename;
    private String password;
    private String sex;
    private String avatar;
    private Integer status;
    private Date createTime;
    private Date loginTime;
    private String phone;
    private String address;
    private String note;
    @TableField(exist = false)
    private Set<String> permissions;
    @TableField(exist = false)
    private List<String> roleNameList;
    @TableField(exist = false)
    private List<GrantedAuthority> grantedAuthorities;

    public List<String> getRoleNameList() {
        return roleNameList;
    }

    public void setRoleNameList(List<String> roleNameList) {
        this.roleNameList = roleNameList;
    }

    public User() {
    }


    public User(String username, String password, String permissions) {
        this.username = username;
        this.password = password;
        grantedAuthorities = new ArrayList<>();
        //将字符串权限，封装为simpleGrantedAuthority对象
        for (String permission : permissions.split(",")) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
            grantedAuthorities.add(simpleGrantedAuthority);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    public User setPermissions(Set<String> permissions) {
        grantedAuthorities = new ArrayList<>();
        for (String permission : permissions) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
            grantedAuthorities.add(simpleGrantedAuthority);
        }
        this.permissions = permissions;
        //为了满足链式编程思想，所以返回this
        return this;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
