package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeeModel {

    private int id;

    private String firstName;

    private String lastName;

    private int experience;

    private int salary;
}
