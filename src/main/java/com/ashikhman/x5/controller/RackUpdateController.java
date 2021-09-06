package com.ashikhman.x5.controller;

import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.command.PutOffRackCellCommand;
import com.ashikhman.x5.command.PutOnRackCellCommand;
import com.ashikhman.x5.entity.ProductEntity;
import com.ashikhman.x5.entity.RackCellEntity;
import com.ashikhman.x5.repository.ProductRepository;
import com.ashikhman.x5.repository.RackCellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Order(0)
@RequiredArgsConstructor
public class RackUpdateController extends AbstractController {

    private final ProductRepository productRepository;
    private final RackCellRepository rackCellRepository;
    private Integer productIndex = 0;

    @Override
    public List<CommandInterface> execute() {
        var commands = new ArrayList<CommandInterface>();
        var products = productRepository.getAll();
        products.sort(Comparator.comparingDouble(ProductEntity::getStockPrice).reversed());

        var cellsByVisibility = rackCellRepository.getAllGroupedByVisibility();
        commands.addAll(fillCellsWithExpensiveProducts(cellsByVisibility.get(5), products));
        //commands.addAll(fillCellsWithExpensiveProducts(cellsByVisibility.get(4), products));
        //commands.addAll(fillCellsWithExpensiveProducts(cellsByVisibility.get(3), products));
        //commands.addAll(fillCellsWithExpensiveProducts(cellsByVisibility.get(2), products));
        //commands.addAll(fillCellsWithExpensiveProducts(cellsByVisibility.get(1), products));

//        commands.addAll(fillCellsWithRandomProducts(cellsByVisibility.get(5), products));
//        commands.addAll(fillCellsWithRandomProducts(cellsByVisibility.get(4), products));
//        commands.addAll(fillCellsWithRandomProducts(cellsByVisibility.get(3), products));
//        commands.addAll(fillCellsWithRandomProducts(cellsByVisibility.get(2), products));
//        commands.addAll(fillCellsWithRandomProducts(cellsByVisibility.get(1), products));

        return commands;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    private List<CommandInterface> fillCellsWithRandomProducts(List<RackCellEntity> cells, List<ProductEntity> products) {
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

    private List<CommandInterface> fillCellsWithExpensiveProducts(List<RackCellEntity> cells, List<ProductEntity> products) {
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

    private ProductEntity getNextProduct(List<ProductEntity> products) {
        if (productIndex >= products.size()) {
            productIndex = 0;
        }

        return products.get(productIndex++);
    }

    private double calculatePrice(ProductEntity product, RackCellEntity cell) {
        var charge = 0.0;
        switch (cell.getVisibility()) {
            case 1:
                charge = -20.0;
                break;
            case 2:
                charge = 21.0;
                break;
            case 3:
                charge = 21.0;
                break;
            case 4:
                charge = 21.0;
                break;
            case 5:
                charge = 22.0;
                break;
        }

        // 20 = 4415222.00
        // 30 = 3329014.80
        //return product.getStockPrice() + product.getStockPrice() / 100.0 * charge;

        //return product.getStockPrice() + product.getStockPrice() / 100.0 * ThreadLocalRandom.current().nextDouble(-20.0, 20.0);

        return product.getStockPrice() + product.getStockPrice() / 100.0 * 15;
    }
}
