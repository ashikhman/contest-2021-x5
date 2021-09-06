package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VisitProductEntity {

    private int productId;

    private double price;

    private int count;
}
