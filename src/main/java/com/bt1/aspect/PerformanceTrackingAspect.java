package com.bt1.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceTrackingAspect {

    /**
     * Đo chính xác thời gian thực thi của inspectInventory (microseconds -> milliseconds)
     */
    @Around("execution(com.bt1.dto.InventorySummary com.bt1.service.ProductService.inspectInventory())")
    public Object trackPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        long startNano = System.nanoTime();

        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long endTime = System.currentTimeMillis();
            long endNano = System.nanoTime();

            long durationMs = endTime - startTime;
            long durationMicros = (endNano - startNano) / 1000;

            System.out.println("============================================");
            System.out.println("PERFORMANCE TRACKING: inspectInventory");
            System.out.println("Duration (milliseconds): " + durationMs + " ms");
            System.out.println("Duration (microseconds): " + durationMicros + " µs");
            System.out.println("Duration (nanoseconds): " + (endNano - startNano) + " ns");
            System.out.println("============================================");
        }
    }
}

