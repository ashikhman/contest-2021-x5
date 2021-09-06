package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GrabEntity {

    private int tick;

    private int productId;

    private int customerId;

    private double price;
}
