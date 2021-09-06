package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckoutEntity {

    private int customerId;

    private int lineId;

    private int startTick;

    private double basketPrice;
}
