// 文件路径: src/main/java/com/yusheng/service/impl/BedServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.pojo.Bed;
import com.yusheng.pojo.Elder;
import com.yusheng.repository.BedRepository;
import com.yusheng.repository.ElderRepository;
import com.yusheng.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private ElderRepository elderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Bed> findBeds(String searchTerm) {
        if (StringUtils.hasText(searchTerm)) {
            return bedRepository.findByRoomNumberContainingOrBedNumberContaining(searchTerm, searchTerm);
        }
        return bedRepository.findAll(Sort.by(Sort.Direction.ASC, "roomNumber", "bedNumber"));
    }

    @Override
    @Transactional
    public Bed save(Bed bed) {
        // ✅ 新增时，将状态设置为字符串 "空闲"
        bed.setStatus("空闲");
        return bedRepository.save(bed);
    }

    @Override
    @Transactional
    public Bed update(Bed bed) {
        if (bed.getId() == null || !bedRepository.existsById(bed.getId())) {
            throw new RuntimeException("更新失败：找不到ID为 " + bed.getId() + " 的床位。");
        }
        return bedRepository.save(bed);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        Bed bed = findById(id);
        // ✅ 使用字符串进行比较
        if (bed != null && "占用".equals(bed.getStatus())) {
            throw new RuntimeException("删除失败：该床位正在被使用，无法删除。");
        }
        bedRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Bed findById(Integer id) {
        return bedRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Bed checkout(Integer bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + bedId + " 的床位"));

        if ("空闲".equals(bed.getStatus())) {
            throw new RuntimeException("操作失败：该床位已经是空闲状态。");
        }

        // ✅✅✅ 核心修复：使用 bedNumber 字符串来查找关联的老人 ✅✅✅
        Optional<Elder> elderOptional = elderRepository.findByBedNumber(bed.getBedNumber());

        if (elderOptional.isPresent()) {
            Elder elder = elderOptional.get();
            elder.setBedNumber(null); // 解除老人的床位号关联
            elderRepository.save(elder); // 保存更新后的老人信息
        }

        bed.setStatus("空闲");
        return bedRepository.save(bed);
    }
}