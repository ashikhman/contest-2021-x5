package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductModel {

    private int id;

    private String name;

    private double stockPrice;

    private int quantity;

    private double sellPrice;
}
