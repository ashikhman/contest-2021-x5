package com.ashikhman.x5.entity;

import com.ashikhman.x5.client.api.model.Customer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class CustomerEntity {

    private int id;

    private Customer.ModeEnum mode;

    private int appearedAt;

    @Nullable
    private Integer disappearedAt;

    private Map<Integer, BasketItemEntity> basket = new HashMap<>();
}
