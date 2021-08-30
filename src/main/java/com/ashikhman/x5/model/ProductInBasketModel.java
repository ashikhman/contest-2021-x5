package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductInBasketModel {

    private ProductModel product;

    private int quantity;

    private double price;
}
