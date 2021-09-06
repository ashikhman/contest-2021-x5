package com.ashikhman.x5.controller;

import com.ashikhman.x5.command.BuyProductCommand;
import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class PrimaryPurchaseController extends AbstractController {

    private final ProductRepository productRepository;

    @Override
    public List<CommandInterface> execute() {
        var commands = new ArrayList<CommandInterface>();
        if (getState().getCurrentTick() != 0) {
            return commands;
        }

        for (var product : productRepository.getAll()) {
            var command = new BuyProductCommand()
                    .setProduct(product)
                    .setQuantity(10000);

            commands.add(command);
        }

        return commands;
    }
}
