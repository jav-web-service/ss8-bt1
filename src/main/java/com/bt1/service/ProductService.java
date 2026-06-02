package com.bt1.service;

import com.bt1.dto.InventorySummary;
import com.bt1.entity.Product;
import com.bt1.exception.InsufficientStockException;
import com.bt1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Nhập thêm hàng vào kho dựa trên SKU
     */
    public void stockIn(String sku, Integer quantity) {
        // Kiểm tra sản phẩm tồn tại
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại với SKU: " + sku);
        }

        // Cập nhật số lượng trực tiếp qua JPQL
        productRepository.incrementQuantity(sku, quantity);
    }

    /**
     * Xuất hàng khỏi kho dựa trên SKU
     */
    public void stockOut(String sku, Integer quantity) {
        // Kiểm tra sản phẩm tồn tại
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại với SKU: " + sku);
        }

        // Kiểm tra số lượng có đủ không
        if (product.get().getQuantity() < quantity) {
            throw new InsufficientStockException(
                "Không đủ hàng để xuất. Hiện có: " + product.get().getQuantity() + ", yêu cầu: " + quantity
            );
        }

        // Cập nhật số lượng trực tiếp qua JPQL
        int updated = productRepository.decrementQuantity(sku, quantity);
        if (updated == 0) {
            throw new InsufficientStockException("Không đủ hàng để xuất");
        }
    }

    /**
     * Kiểm kê kho - tính tổng số lượng và giá trị
     */
    public InventorySummary inspectInventory() {
        Integer totalQuantity = productRepository.getTotalQuantity();
        Double totalValue = productRepository.getTotalValue();

        return new InventorySummary(totalQuantity, totalValue);
    }

    /**
     * Xóa sản phẩm theo ID
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Lấy sản phẩm theo SKU
     */
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    /**
     * Tạo sản phẩm mới
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}

