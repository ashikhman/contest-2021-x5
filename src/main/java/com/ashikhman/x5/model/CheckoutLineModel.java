package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class CheckoutLineModel {

    private int id;

    @Nullable
    private EmployeeModel employee;

    @Nullable
    private CustomerModel customer;
}
