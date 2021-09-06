package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class ProductEntity {

    private int id;

    private String name;

    private double stockPrice;

    private int quantity;

    private int totalQuantity;

    private double sellPrice;

    @Nullable
    private RackCellEntity rackCell;
}
