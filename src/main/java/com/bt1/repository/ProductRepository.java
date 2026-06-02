package com.bt1.repository;

import com.bt1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity + :quantity WHERE p.sku = :sku")
    int incrementQuantity(@Param("sku") String sku, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.sku = :sku AND p.quantity >= :quantity")
    int decrementQuantity(@Param("sku") String sku, @Param("quantity") Integer quantity);

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p")
    Integer getTotalQuantity();

    @Query("SELECT COALESCE(SUM(p.quantity * p.price), 0.0) FROM Product p")
    Double getTotalValue();
}

