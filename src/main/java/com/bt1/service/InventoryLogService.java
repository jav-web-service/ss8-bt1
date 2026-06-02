package com.bt1.service;

import com.bt1.entity.InventoryLog;
import com.bt1.repository.InventoryLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;

    @Transactional
    public void logActivity(String username, String action, String detail) {
        InventoryLog log = new InventoryLog();
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setAction(action);
        log.setDetail(detail);

        inventoryLogRepository.save(log);
    }
}

