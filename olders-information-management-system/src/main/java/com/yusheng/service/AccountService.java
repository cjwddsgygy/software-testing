// 文件路径: src/main/java/com/yusheng/service/AccountService.java
package com.yusheng.service;

import com.yusheng.pojo.Admin;
import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Elder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class AccountService {

    @Autowired private AdminService adminService;
    @Autowired private CareWorkersService careWorkersService;
    @Autowired private ElderService elderService;
    // 假设无加密器

    @Transactional
    public Object updateUserInfo(Authentication authentication, Map<String, Object> updates) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Admin) {
            Admin currentUser = (Admin) principal;
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name": currentUser.setName((String) value); break;
                    case "password":
                        if (value instanceof String && !((String) value).isEmpty()) {
                            currentUser.setPassword((String) value);
                        }
                        break;
                }
            });
            adminService.update(currentUser);
            return currentUser;
        }

        if (principal instanceof CareWorker) {
            CareWorker currentUser = (CareWorker) principal;
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name": currentUser.setName((String) value); break;
                    case "age": currentUser.setAge(Integer.parseInt(String.valueOf(value))); break;
                    case "birthDate": currentUser.setBirthDate(LocalDate.parse((String) value)); break;
                    case "ethnicity": currentUser.setEthnicity((String) value); break;
                    case "education": currentUser.setEducation((String) value); break;
                    case "experience": currentUser.setExperience((String) value); break;
                    case "specialties": currentUser.setSpecialties((String) value); break;
                    case "status": currentUser.setStatus((String) value); break;
                    case "password":
                        if (value instanceof String && !((String) value).isEmpty()) {
                            currentUser.setPassword((String) value);
                        }
                        break;
                }
            });
            careWorkersService.update(currentUser);
            return currentUser;
        }

        if (principal instanceof Elder) {
            Elder currentUser = (Elder) principal;
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name": currentUser.setName((String) value); break;
                    case "age": currentUser.setAge(Integer.parseInt(String.valueOf(value))); break;
                    case "birthDate": currentUser.setBirthDate(LocalDate.parse((String) value)); break;
                    case "ethnicity": currentUser.setEthnicity((String) value); break;
                    case "education": currentUser.setEducation((String) value); break;
                    case "maritalStatus": currentUser.setMaritalStatus((String) value); break;
                    case "hobbies": currentUser.setHobbies((String) value); break;
                    case "relativeContact": currentUser.setRelativeContact((String) value); break;
                    case "password":
                        if (value instanceof String && !((String) value).isEmpty()) {
                            currentUser.setPassword((String) value);
                        }
                        break;
                }
            });
            elderService.update(currentUser);
            return currentUser;
        }

        throw new RuntimeException("无法识别的用户类型，更新失败。");
    }
}