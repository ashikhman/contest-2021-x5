package com.ashikhman.x5.entity;

import com.ashikhman.x5.client.api.model.Customer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class VisitEntity {

    private int startTick;

    private int endTick;

    private int waitCheckoutStartTick;

    private int waitCheckoutEndTick;

    private int atCheckoutStartTick;

    private int customerId;

    private Customer.ModeEnum mode;

    private List<VisitProductEntity> products = new ArrayList<>();
}
