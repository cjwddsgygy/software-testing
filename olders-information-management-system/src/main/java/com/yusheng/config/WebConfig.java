// 文件路径: src/main/java/com/yusheng/config/WebConfig.java
package com.yusheng.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 从 application.properties 注入文件存储的物理路径
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 从 application.properties 注入外部访问的URL前缀
    @Value("${file.access-path}")
    private String accessPath;

    /**
     * ✅✅✅ 核心配置：添加静态资源处理器 ✅✅✅
     * 这段代码的作用是，建立一个 URL 路径和磁盘物理路径之间的映射关系。
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 当浏览器请求的 URL 以 /uploads/ 开头时...
        registry.addResourceHandler(accessPath)
                // ...就去 file:D:/yusheng-uploads/ 这个物理路径下查找对应的文件
                .addResourceLocations("file:" + uploadDir);
    }

}