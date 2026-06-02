package com.bt1.aspect;

import com.bt1.exception.SecurityException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SecurityCheckAspect {

    /**
     * Kiểm tra quyền ADMIN trước khi xóa sản phẩm
     * Nếu user không phải ADMIN, ném ra SecurityException
     */
    @Before("execution(void com.bt1.service.ProductService.deleteProduct(..)) && args(id)")
    public void checkAdminRole(JoinPoint joinPoint, Long id) {
        String userRole = getUserRoleFromHeader();

        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            throw new SecurityException(
                "Quyền bị từ chối: Chỉ ADMIN có thể xóa sản phẩm. Role hiện tại: " + userRole
            );
        }
    }

    private String getUserRoleFromHeader() {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            return attributes.getRequest().getHeader("X-Role");
        }
        return null;
    }
}

