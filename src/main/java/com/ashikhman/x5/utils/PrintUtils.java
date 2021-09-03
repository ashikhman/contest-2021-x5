package com.ashikhman.x5.utils;

import com.ashikhman.x5.model.GameStateModel;
import com.ashikhman.x5.service.GameStateHolder;

import java.util.StringJoiner;

public class PrintUtils {

    private PrintUtils() {
    }

    public static void printCheckoutLines(GameStateModel state) {
        var output = new StringBuilder();
        output.append("CHLINES: ");

        var joiner = new StringJoiner("|");
        state.getCheckoutLines().entrySet().forEach(entry -> {
            var line = entry.getValue();
            joiner.add(String.valueOf(line.getId()));
        });

        output.append(joiner);
        output.append("@");

        System.out.println(output);
    }

    public static void printProducts(GameStateModel state) {
        var output = new StringBuilder();
        output.append("PRODUCTS: ");

        var joiner = new StringJoiner("|");
        for (var entry : state.getProducts().entrySet()) {
            var product = entry.getValue();
            joiner.add(String.format("%s,%s,%s", product.getId(), product.getName(), product.getStockPrice()));
        }

        output.append(joiner);
        output.append("@");

        System.out.println(output);
    }

    public static void printOffers(GameStateModel state) {
        var output = new StringBuilder();
        output.append("OFFERS: ");

        var joiner = new StringJoiner("|");
        for (var offer : state.getRecruitmentOffers()) {
            joiner.add(String.format("%s,%s,%s", offer.getEmployeeType(), offer.getExperience(), offer.getSalary()));
        }

        output.append(joiner);
        output.append("@");

        System.out.println(output);
    }

    public static void printCustomers(GameStateModel state) {
        var output = new StringBuilder();
        output.append("CUSTOMERS");

//        var joiner = new StringJoiner("|");
//        for (var entry : state.getCustomers().entrySet()) {
//            var customer = entry.getValue();
//            joiner.add(String.format(
//                    "%s",
//                    customer.getId(),
//                    customer.get
//            ));
//        }
    }

    public static void printCommands(GameStateHolder stateHolder) {
        var output = new StringBuilder();
        output.append("COMMANDS: ");

        var stateJoiner = new StringJoiner("|");
        for (var state : stateHolder.getHistory()) {
            if (state.getCommands().isEmpty()) {
                continue;
            }

            var commandJoiner = new StringJoiner(";");
            for (var command : state.getCommands()) {
                commandJoiner.add(command.toString());
            }
            stateJoiner.add(String.format("[%s]%s", state.getCurrentTick(), commandJoiner));
        }

        output.append(stateJoiner);
        output.append("@");

        System.out.println(output);
    }

    public static void printTicks(GameStateModel state) {
        System.out.printf("TOTALTICKS: %s\n", state.getTickCount());
    }

    public static void printGameOver(GameStateModel state) {
        var template = "GameOver - Income: %s, " +
                "Salary costs: %s, " +
                "Stock costs: %s, " +
                "Profit: %s @\n";
        System.out.printf(
                template,
                state.getIncome(),
                state.getSalaryCosts(),
                state.getStockCosts(),
                state.getIncome() - state.getSalaryCosts() - state.getStockCosts()
        );
    }
}
