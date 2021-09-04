package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class RackCellModel {

    private int id;

    private int visibility;

    private int capacity;

    @Nullable
    private ProductModel product;

    @Nullable
    private String productName;

    private int productQuantity;

    private int productTime;
}
