package com.ashikhman.x5.service.controller;

import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.command.PutOnRackCellCommand;
import com.ashikhman.x5.model.GameStateModel;
import com.ashikhman.x5.model.ProductModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

@Service
public class RackUpdateController implements ControllerInterface {

    @Override
    public List<CommandInterface> execute(GameStateModel state) {
//        if (state.getCurrentTick() != 0) {
//            return new ArrayList<>();
//        }

        var products = new Stack<ProductModel>();
        for (var entry : state.getProducts().entrySet()) {
            products.add(entry.getValue());
        }
        Collections.shuffle(products);

        var commands = new ArrayList<CommandInterface>();
        for (var entry : state.getRackCells().entrySet()) {
            var cell = entry.getValue();
            var product = products.pop();
            var command = new PutOnRackCellCommand()
                    .setProduct(product)
                    .setQuantity(product.getQuantity())
                    .setSellPrice(product.getStockPrice() + product.getStockPrice() / 100.0 * 30.0)
                    .setCell(cell);

            commands.add(command);
        }

        return commands;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
