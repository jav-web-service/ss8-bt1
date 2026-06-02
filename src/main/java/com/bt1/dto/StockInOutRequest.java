package com.bt1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockInOutRequest {

    @NotBlank(message = "SKU không được để trống")
    private String sku;

    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}

