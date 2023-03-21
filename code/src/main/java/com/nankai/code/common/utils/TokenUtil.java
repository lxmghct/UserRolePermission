package com.nankai.code.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nankai.code.common.User;
import java.util.Date;


public class TokenUtil {

    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000;
    private static final String TOKEN_SECRET = "txdy"; //密钥盐


    //签名生成
    public static String sign(User user, String role, String permission) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);

            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("user_id", user.getId())
                    .withClaim("user_name", user.getUsername())
                    .withClaim("role", role)
                    .withClaim("permission", permission)
                    .withExpiresAt(expiresAt)
                    //使用了HMAC256加密算法
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }


    //根据token返回用户名
    public static String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("user_name").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    //根据token返回密码
    public static String getPasswordFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("password").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    //根据token返回权限
    public static String getPermissionFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("permission").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    //根据token返回角色
    public static String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("role").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

}