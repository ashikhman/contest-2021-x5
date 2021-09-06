package com.ashikhman.x5.service;

import com.ashikhman.x5.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultPrinter {

    private final ProductRepository productRepository;

    private final StateRepository stateRepository;

    private final VisitRepository visitRepository;

    private final CheckoutRepository checkoutRepository;

    private final GrabRepository grabRepository;

    public void print() {
        var state = stateRepository.get();

        var soldProductsStockCosts = getSoldProductsStockCost();
        System.out.printf("Income: %.2f\n", state.getIncome());
        System.out.printf("Salary costs: %.2f\n", state.getSalaryCosts());
        System.out.printf("Sold products stock cost: %.2f\n", soldProductsStockCosts);
        System.out.printf("Sold products profit: %.2f\n", state.getIncome() - soldProductsStockCosts);

        //printVisits();
        printCheckouts();

        grabRepository.printStats();
    }

    private void printCheckouts() {
        var totalPrice = 0.0;
        for (var checkout : checkoutRepository.getAll()) {
            totalPrice += checkout.getBasketPrice();
        }

        System.out.printf("CHECKOUTS: %s\n", checkoutRepository.getAll().size());
        System.out.printf("CHECKOUTS PRICE: %.2f\n", totalPrice);
    }

    private void printVisits() {
        var price = 0.0;
        for (var visit : visitRepository.getAll()) {
            for (var product : visit.getProducts()) {
                price += product.getPrice() * product.getCount();
            }
        }

        System.out.printf("Visits: %s\n", visitRepository.getAll().size());
        System.out.printf("Visits price: %.2f\n", price);
    }

    private double getSoldProductsStockCost() {
        var cost = 0.0;
        for (var product : productRepository.getAll()) {
            var soldCount = product.getTotalQuantity() - product.getQuantity();
            cost += soldCount * product.getStockPrice();
        }

        return cost;
    }
}
