package com.yusheng;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

//    生成JWT令牌
    @Test
    public void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("account", "yushengwang");

        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256,
                        "eXVzaGVuZw==") //指定加密算法、密钥
                .addClaims(dataMap) //添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000)) //设置过期时间
                .compact(); //生成令牌
        System.out.println(jwt);
    }

//    解析JWT令牌
    @Test
    public void testParseJWT() {
        String token = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJpZCI6MSwiYWNjb3VudCI6Inl1c2hlbmd3YW5nIiwiZXhwIjoxNzUwODU2MjI4fQ." +
                "jRNLB2eBiG4COM416eKQR9qpz1EucOmq7v535DDlfg8";
        Claims claims = Jwts.parser().setSigningKey("eXVzaGVuZw==") //指定密钥
                .parseClaimsJws(token) //解析令牌
                .getBody(); //获取自定义信息
        System.out.println(claims);
    }
}
