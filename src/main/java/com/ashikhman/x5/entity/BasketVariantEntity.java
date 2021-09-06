package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BasketVariantEntity {

    private String variant;

    private int id;

    private int count;
}
