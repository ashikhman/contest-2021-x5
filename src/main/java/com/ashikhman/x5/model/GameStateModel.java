package com.ashikhman.x5.model;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.command.CommandInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
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

    private List<CommandInterface> commands = new ArrayList<>();

    public GameStateModel(GameStateModel state) {
        this.worldResponse = state.getWorldResponse();
        this.tickCount = state.getTickCount();
        this.currentTick = state.getCurrentTick();
        this.commands = List.copyOf(state.getCommands());

        for (var entry : state.getCustomers().entrySet()) {
            this.customers.put(entry.getKey(), new CustomerModel(entry.getValue()));
        }
    }
}
