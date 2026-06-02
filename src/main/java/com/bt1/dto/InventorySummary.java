package com.bt1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummary {
    private Integer totalQuantity;
    private Double totalValue;
}

