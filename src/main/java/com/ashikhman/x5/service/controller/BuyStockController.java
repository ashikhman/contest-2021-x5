package com.ashikhman.x5.service.controller;

import com.ashikhman.x5.command.BuyStockCommand;
import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.model.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuyStockController implements ControllerInterface {

    @Override
    public List<CommandInterface> execute(GameStateModel state) {
        if (state.getCurrentTick() != 0) {
            return new ArrayList<>();
        }

        var commands = new ArrayList<CommandInterface>();
        for (var entry : state.getProducts().entrySet()) {
            var product = entry.getValue();
            var command = new BuyStockCommand()
                    .setProduct(product)
                    .setQuantity(10000);

            product.setQuantity(product.getQuantity() + command.getQuantity());

            commands.add(command);
        }

        return commands;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
