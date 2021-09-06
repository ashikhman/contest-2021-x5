package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class RackCellEntity {

    private int id;

    private int visibility;

    private int capacity;

    @Nullable
    private ProductEntity product;

    @Nullable
    private Integer productId;

    @Nullable
    private String productName;

    @Nullable
    private Integer productQuantity;
}
