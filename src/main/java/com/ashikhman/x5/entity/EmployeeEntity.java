package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
public class EmployeeEntity {

    private int id;

    private String firstName;

    private String lastName;

    private int experience;

    private int salary;

    private int workingTicks = 1;

    private int vacationTicks = 1;

    @Nullable
    private CheckoutLineEntity checkoutLine;
}
