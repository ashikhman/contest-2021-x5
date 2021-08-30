package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class GameStateModel {

    private int tickCount;

    private int currentTick;

    private double income;

    private double salaryCosts;

    private double stockCosts;

    private boolean gameOver;

    private Map<Integer, CheckoutLineModel> checkoutLines = new HashMap<>();

    private Map<Integer, EmployeeModel> employees = new HashMap<>();

    private Map<Integer, CustomerModel> customers = new HashMap<>();

    private List<RecruitmentOfferModel> recruitmentOffers = new ArrayList<>();

    private Map<Integer, ProductModel> products = new HashMap<>();

    private Map<Integer, RackCellModel> rackCells = new HashMap<>();
}
