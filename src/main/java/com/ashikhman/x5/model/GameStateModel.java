package com.ashikhman.x5.model;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class GameStateModel {

    private CurrentWorldResponse worldResponse;

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

    public void print() {
        System.out.print("PRODUCTS: ");
        for (var entry : products.entrySet()) {
            var product = entry.getValue();
            System.out.print(product.getStockPrice() + "|");
        }
        System.out.print("@ ");
        System.out.println();
    }

    public void printResult() {
        var template = "Income: %s\n" +
                "Salary costs: %s\n" +
                "Stock costs: %s\n" +
                "Profit: %s\n";
        System.out.printf(
                template,
                income,
                salaryCosts,
                stockCosts,
                income - salaryCosts - stockCosts
        );
    }
}
