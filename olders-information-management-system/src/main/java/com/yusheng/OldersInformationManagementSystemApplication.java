package com.yusheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.yusheng.repository") // 告诉JPA去哪里找Repository
@EntityScan(basePackages = "com.yusheng.pojo")          // 告诉JPA去哪里找实体类
public class OldersInformationManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OldersInformationManagementSystemApplication.class, args);
    }

}