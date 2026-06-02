package com.bt1.aspect;

import com.bt1.service.InventoryLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ActivityLoggingAspect {

    private final InventoryLogService inventoryLogService;

    public ActivityLoggingAspect(InventoryLogService inventoryLogService) {
        this.inventoryLogService = inventoryLogService;
    }

    /**
     * Ghi log sau khi stockIn hoàn thành thành công
     */
    @AfterReturning("execution(void com.bt1.service.ProductService.stockIn(..)) && args(sku, quantity)")
    public void logStockIn(JoinPoint joinPoint, String sku, Integer quantity) {
        String username = getUsernameFromHeader();
        String detail = String.format("[%s] - User: %s performed Stock In successfully. Quantity changed: +%d",
            System.currentTimeMillis(), username, quantity);

        inventoryLogService.logActivity(username, "STOCK_IN", detail);
    }

    /**
     * Ghi log sau khi stockOut hoàn thành thành công
     */
    @AfterReturning("execution(void com.bt1.service.ProductService.stockOut(..)) && args(sku, quantity)")
    public void logStockOut(JoinPoint joinPoint, String sku, Integer quantity) {
        String username = getUsernameFromHeader();
        String detail = String.format("[%s] - User: %s performed Stock Out successfully. Quantity changed: -%d",
            System.currentTimeMillis(), username, quantity);

        inventoryLogService.logActivity(username, "STOCK_OUT", detail);
    }

    private String getUsernameFromHeader() {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            return attributes.getRequest().getHeader("X-User");
        }
        return "UNKNOWN";
    }
}

