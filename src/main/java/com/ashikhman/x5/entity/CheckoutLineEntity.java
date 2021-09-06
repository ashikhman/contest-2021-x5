package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class CheckoutLineEntity {

    private int id;

    @Nullable
    private EmployeeEntity employee;

    @Nullable
    private CustomerEntity customer;

    @Nullable
    private Integer employeeId;

    @Nullable
    private Integer customerId;
}
