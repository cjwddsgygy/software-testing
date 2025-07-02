// 文件路径: src/main/java/com/yusheng/controller/FileUploadController.java
package com.yusheng.controller;

import com.yusheng.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files") // 我们将所有文件相关的操作都放在 /api/files 路径下
public class FileUploadController {

    // 从 application.properties 中注入文件存储的物理路径
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-path}")
    private String accessPath;
    /**
     * 处理文件上传请求
     * 对应前端请求: POST /api/files/upload
     * @param file 前端通过 FormData 发送过来的文件，参数名必须是 "file"
     * @return 包含文件可访问 URL 的 Result 对象
     */
    @PostMapping("/uploads")
    @PreAuthorize("hasRole(\"ADMIN\")")
    public Result<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error("上传失败，请选择一个文件。");
        }

        try {
            // 1. 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 2. 生成一个唯一的随机文件名，防止重名覆盖
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 3. 确保上传目录存在，如果不存在则创建
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建多级目录
            }

            // 4. 构建文件的完整存储路径
            Path filePath = Paths.get(uploadDir, uniqueFileName);

            // 5. 将上传的文件流复制到目标路径
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            String serverBaseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            // 6. 构建前端可以通过 URL 访问的路径
            //    例如：/uploads/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx.png
            //    前端会把它拼接成：http://localhost:8080/uploads/....
            String fileAccessUrl = serverBaseUrl + "/uploads/" + uniqueFileName;

            System.out.println("Generated full image URL: " + fileAccessUrl);

            // 7. 将 URL 封装到 Map 中，再放入 Result 对象返回给前端
            Map<String, String> data = new HashMap<>();
            data.put("url", fileAccessUrl);

            return Result.success(data);

        } catch (IOException e) {
            e.printStackTrace();
            // 在实际项目中，这里应该记录更详细的错误日志
            return Result.error("文件上传失败，服务器内部错误。");
        }
    }
}