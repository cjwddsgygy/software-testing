package com.yusheng;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * 这是一个用于基本上下文加载的测试。
 * 为了避免在构建过程中因依赖外部服务（如 Redis）而失败，
 * 我们使用 @TestPropertySource 来覆盖 application.yml 中的配置。
 */
@SpringBootTest
@TestPropertySource(properties = {
        // 禁用 Spring Security 的自动配置，让 SecurityConfig 和我们的 Filter 不被加载
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",

        // 禁用 Redis 的自动配置，这样就不会去尝试连接 Redis
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
class OldersInformationManagementSystemApplicationTests {

    @Test
    void contextLoads() {
        // 这个测试的目的是验证 Spring 的核心上下文能否在没有安全和 Redis 的情况下加载。
        // 如果这个测试通过，意味着你的基本 IoC/DI 配置是正确的。
        System.out.println("Basic context loaded successfully without Security and Redis!");
    }

}
