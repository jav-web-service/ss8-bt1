package com.bt1.controller;

import com.bt1.dto.ApiResponse;
import com.bt1.dto.InventorySummary;
import com.bt1.dto.StockInOutRequest;
import com.bt1.exception.InsufficientStockException;
import com.bt1.exception.SecurityException;
import com.bt1.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * POST /api/products/stock-in
     * Nhập thêm hàng vào kho
     */
    @PostMapping("/stock-in")
    public ResponseEntity<?> stockIn(
            @Valid @RequestBody StockInOutRequest request,
            @RequestHeader("X-User") String username,
            @RequestHeader("X-Role") String role) {
        try {
            productService.stockIn(request.getSku(), request.getQuantity());
            return ResponseEntity.ok(
                new ApiResponse(true, "Nhập hàng thành công. Số lượng: +" + request.getQuantity())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Lỗi: " + e.getMessage()));
        }
    }

    /**
     * POST /api/products/stock-out
     * Xuất hàng khỏi kho
     */
    @PostMapping("/stock-out")
    public ResponseEntity<?> stockOut(
            @Valid @RequestBody StockInOutRequest request,
            @RequestHeader("X-User") String username,
            @RequestHeader("X-Role") String role) {
        try {
            productService.stockOut(request.getSku(), request.getQuantity());
            return ResponseEntity.ok(
                new ApiResponse(true, "Xuất hàng thành công. Số lượng: -" + request.getQuantity())
            );
        } catch (InsufficientStockException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Lỗi: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Lỗi: " + e.getMessage()));
        }
    }

    /**
     * GET /api/products/inspect
     * Tính tổng số lượng và giá trị kho
     */
    @GetMapping("/inspect")
    public ResponseEntity<?> inspectInventory(
            @RequestHeader("X-User") String username,
            @RequestHeader("X-Role") String role) {
        InventorySummary summary = productService.inspectInventory();
        return ResponseEntity.ok(
            new ApiResponse(true, "Kiểm kê kho thành công", summary)
        );
    }

    /**
     * DELETE /api/products/{id}
     * Xóa sản phẩm (Chỉ ADMIN)
     * AOP sẽ kiểm tra quyền trước khi thực thi
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("X-User") String username,
            @RequestHeader("X-Role") String role) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(
                new ApiResponse(true, "Xóa sản phẩm thành công")
            );
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }

    /**
     * Exception handler cho lỗi validation
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
            .body(new ApiResponse(false, "Validation error: " + message));
    }
}

