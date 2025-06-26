package com.yusheng.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 密钥（与测试类中保持一致）
    private static final String SECRET_KEY = "eXVzaGVuZw==";

    // 12小时的毫秒数
    private static final long EXPIRATION = 12 * 60 * 60 * 1000;

    /**
     * 生成JWT令牌
     *
     * @param payload 要嵌入到token中的数据
     * @return 返回生成的token字符串
     */
    public static String generateToken(Map<String, Object> payload) {
        return Jwts.builder()
                .addClaims(payload) // 添加自定义声明
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 签名算法和密钥
                .compact(); // 构建并返回字符串形式的token
    }

    /**
     * 解析JWT令牌
     *
     * @param token 需要解析的token字符串
     * @return 返回token中的声明（payload）
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // 设置签名密钥
                .parseClaimsJws(token) // 解析token
                .getBody(); // 获取负载内容
    }
}