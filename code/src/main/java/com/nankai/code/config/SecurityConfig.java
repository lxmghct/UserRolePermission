package com.nankai.code.config;

import com.nankai.code.security.encode.SecurityPasswordEncoder;
import com.nankai.code.security.filter.JwtAuthenticationTokenFilter;
import com.nankai.code.security.handler.AnonymousAuthenticationEntryPoint;
import com.nankai.code.security.handler.UserAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint;

    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;

    @Autowired
    private CorsFilter corsFilter;

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new SecurityPasswordEncoder();
    }


    /**
     * 用于校验Authentication，返回一个认证完成后的Authentication对象
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //csrf
        http.csrf().disable();
        //禁用cors
        http.cors().disable();
        //过滤请求
        http.authorizeRequests()
                //登录允许放行
                .antMatchers("/user/login").permitAll()
                //其他任何请求需要验证权限
                .anyRequest().authenticated()
                //2.异常处理
                .and()
                .exceptionHandling()
                //2.1匿名用户没有登录的处理
                .authenticationEntryPoint(anonymousAuthenticationEntryPoint)
                //2.2登录的用户没有权限的处理
                .accessDeniedHandler(userAccessDeniedHandler);
        //登出
        http.logout()
                .deleteCookies("Authorization");

        // 在UsernamePassword过滤器之前添加JWT过滤器
        // 添加JWTfilter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        http.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        http.addFilterBefore(corsFilter, LogoutFilter.class);
    }
}
