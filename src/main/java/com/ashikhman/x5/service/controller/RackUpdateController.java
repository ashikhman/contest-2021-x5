package com.ashikhman.x5.service.controller;

import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.command.PutOffRackCellCommand;
import com.ashikhman.x5.command.PutOnRackCellCommand;
import com.ashikhman.x5.model.GameStateModel;
import com.ashikhman.x5.model.ProductModel;
import com.ashikhman.x5.model.RackCellModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RackUpdateController implements ControllerInterface {

    private Integer productIndex = 0;

    @Override
    public List<CommandInterface> execute(GameStateModel state) {
        var products = new ArrayList<ProductModel>();
        for (var entry : state.getProducts().entrySet()) {
            products.add(entry.getValue());
        }
        products.sort(Comparator.comparingDouble(ProductModel::getStockPrice).reversed());

        var commands = new ArrayList<CommandInterface>();
        commands.addAll(fillCellsWithExpensiveProducts(state.getRackCells().getByVisibility().get(5), products));
        commands.addAll(fillCellsWithExpensiveProducts(state.getRackCells().getByVisibility().get(4), products));
        commands.addAll(fillCellsWithExpensiveProducts(state.getRackCells().getByVisibility().get(3), products));
        commands.addAll(fillCellsWithExpensiveProducts(state.getRackCells().getByVisibility().get(2), products));
        commands.addAll(fillCellsWithExpensiveProducts(state.getRackCells().getByVisibility().get(1), products));

        //commands.addAll(fillCellsWithRandomProducts(state.getRackCells().getByVisibility().get(1), products));

        return commands;
    }

    private List<CommandInterface> fillCellsWithRandomProducts(List<RackCellModel> cells, List<ProductModel> products) {
        var commands = new ArrayList<CommandInterface>();
        for (var cell : cells) {
            if (null != cell.getProduct()) {
                commands.add(
                        new PutOffRackCellCommand().setCell(cell)
                );
            }

            var product = getNextProduct(products);
            var command = new PutOnRackCellCommand()
                    .setProduct(product)
                    .setQuantity(product.getQuantity())
                    .setSellPrice(calculatePrice(product, cell))
                    .setCell(cell);

            commands.add(command);
        }

        return commands;
    }

    private List<CommandInterface> fillCellsWithExpensiveProducts(List<RackCellModel> cells, List<ProductModel> products) {
        var commands = new ArrayList<CommandInterface>();
        for (var cell : cells) {
            if (null != cell.getProduct()) {
                commands.add(
                        new PutOffRackCellCommand().setCell(cell)
                );
            }

            var product = products.remove(0);
            var command = new PutOnRackCellCommand()
                    .setProduct(product)
                    .setQuantity(product.getQuantity())
                    .setSellPrice(calculatePrice(product, cell))
                    .setCell(cell);

            commands.add(command);
        }

        return commands;
    }

    private ProductModel getNextProduct(List<ProductModel> products) {
        if (productIndex >= products.size()) {
            productIndex = 0;
        }

        return products.get(productIndex++);
    }

    private double calculatePrice(ProductModel product, RackCellModel cell) {
        var charge = 19.0;
//        switch (cell.getVisibility()) {
//            case 1:
//                charge = 21.0;
//                break;
//            case 2:
//                charge = 21.0;
//                break;
//            case 3:
//                charge = 21.0;
//                break;
//            case 4:
//                charge = 21.0;
//                break;
//            case 5:
//                charge = 21.0;
//                break;
//        }

        // 20 = 4415222.00
        // 30 = 3329014.80
        return product.getStockPrice() + product.getStockPrice() / 100.0 * charge;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
