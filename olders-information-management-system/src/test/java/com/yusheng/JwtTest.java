package com.yusheng;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTest {

    // 将密钥和过期时间定义为常量，便于维护
    private static final String SIGN_KEY = "yushengwang"; // 注意：这个密钥太弱了，稍后会提建议
    private static final long EXPIRE_TIME = 1000 * 60 * 60; // 设置为1小时，用于测试

    @Test
    public void testGenerateAndParseJwt() {
        // 1. 准备 Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("account", "testuser");

        // 2. 动态生成 Token
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .compact();

        System.out.println("动态生成的Token: " + jwt);
        assertNotNull(jwt); // 断言Token不为空

        // 3. 立即解析刚刚生成的 Token
        Claims parsedClaims = Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .parseClaimsJws(jwt)
                .getBody();

        System.out.println("解析出的Claims: " + parsedClaims);

        // 4. 断言解析出的内容是否正确
        assertEquals(1, parsedClaims.get("id", Integer.class));
        assertEquals("testuser", parsedClaims.get("account", String.class));
    }
}
