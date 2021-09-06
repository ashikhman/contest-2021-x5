package com.ashikhman.x5.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StateEntity {

    private int tickCount;

    private int currentTick;

    private double income;

    private double salaryCosts;

    private double stockCosts;

    private boolean gameOver;
}
