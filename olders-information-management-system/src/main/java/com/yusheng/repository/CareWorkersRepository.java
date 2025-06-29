// backend/src/main/java/com/yusheng/repository/CareWorkersRepository.java
package com.yusheng.repository; // 注意包名是 com.yusheng.repository

import com.yusheng.pojo.CareWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareWorkersRepository extends JpaRepository<CareWorker, Integer> {

}