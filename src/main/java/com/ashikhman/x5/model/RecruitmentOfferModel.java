package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RecruitmentOfferModel {

    private String experience;

    private String employeeType;

    private int salary;
}
