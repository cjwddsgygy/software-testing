// 文件路径: backend/src/main/java/com/yusheng/utils/JwtUtils.java
package com.yusheng.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils; // 确保导入了 StringUtils

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 使用您自己的密钥和过期时间
    private static final String SECRET_KEY = "eXVzaGVuZw==";
    private static final long EXPIRATION = 12 * 60 * 60 * 1000;

    /**
     * 生成JWT令牌 (保持不变)
     */
    public static String generateToken(Map<String, Object> payload) {
        return Jwts.builder()
                .addClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT令牌 (这是我们修改的核心)
     *
     * @param token 需要解析的token字符串
     * @return 如果token有效，返回token中的声明(payload)；如果token无效或非法，返回 null
     */
    public static Claims parseToken(String token) {
        // 1. 检查输入 token 是否为空或仅包含空白字符
        if (!StringUtils.hasText(token)) {
            return null; // 如果为空，直接返回 null，不抛异常
        }

        // 2. 检查 token 格式是否正确 (必须包含两个 '.')
        if (token.split("\\.").length != 3) {
            System.out.println("Invalid JWT format: " + token);
            return null; // 如果格式不正确，直接返回 null，不抛异常
        }

        try {
            // 3. 只有在通过所有前置检查后，才进行解析
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 4. 如果解析过程中发生任何官方库抛出的异常 (如过期、签名错误)
            //    在后台打印错误日志，并返回 null
            System.out.println("JWT parsing failed. Token: " + token + ", Error: " + e.getMessage());
            return null;
        }
    }
}