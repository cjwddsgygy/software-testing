// 文件路径: backend/src/main/java/com/yusheng/utils/JwtUtils.java
package com.yusheng.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils; // 确保导入了 StringUtils

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 密钥 (保持不变)
    private static final String SECRET_KEY = "eXVzaGVuZw==";

    // 过期时间常量，现在可以不再用于生成Token，但您代码中可能仍有用到，所以可以保留
    // 或者直接删除，取决于您其他地方是否依赖这个常量
    // private static final long EXPIRATION = 12 * 60 * 60 * 1000; // 12小时

    /**
     * 生成JWT令牌 (核心修改在这里：不设置过期时间)
     *
     * @param payload Token中需要包含的声明信息
     * @return 生成的JWT令牌字符串
     *
     * ⚠️ 安全警告：不设置过期时间的Token存在巨大安全风险，如果Token泄露，将永远有效。
     *              在生产环境中强烈建议设置合理的过期时间，并结合刷新机制。
     */
    public static String generateToken(Map<String, Object> payload) {
        return Jwts.builder()
                .addClaims(payload) // 添加载荷
                // .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // <<--- 注释或删除此行，以实现永不过期
                .setIssuedAt(new Date()) // 设置签发时间，推荐保留
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 签名算法和密钥
                .compact();
    }

    /**
     * 解析JWT令牌 (保持不变，因为您的现有实现已经很完善)
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