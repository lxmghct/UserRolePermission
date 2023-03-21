package com.nankai.code.security.filter;

import com.nankai.code.common.User;
import com.nankai.code.common.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //1.拿到Authorization请求头内的信息
        String token = httpServletRequest.getHeader("Authorization");
        //2.如果token不为空
        if (token != null && token.trim().length() != 0 && !"null".equals(token.trim())) {
            //3.从token中获取用户信息
            String username = TokenUtil.getUsernameFromToken(token);
            String password = TokenUtil.getPasswordFromToken(token);
            String permission = TokenUtil.getPermissionFromToken(token);
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(permission)) {
                User user = new User(username, password, permission);
                //4.组装authentication对象，构造参数是Principal Credentials 与 Authorities
                //5.后面的拦截器里面会用到 grantedAuthorities 方法
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                //6.将authentication信息放入到上下文对象中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //7.放行，注意：若token为空，上下文对象中没有authentication，会抛出异常进入AnonymousAuthenticationEntryPoint
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
