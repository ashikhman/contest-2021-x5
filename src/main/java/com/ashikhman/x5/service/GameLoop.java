package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.*;
import com.ashikhman.x5.utils.PrintUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameLoop {

    private final PerfectStoreEndpointApi storeApi;

    private final GameStateHolder stateHolder;

    private final CommandsExecutor commandsExecutor;

    public void run() {
        log.info("Running game loop");

        var state = stateHolder.getState();
        PrintUtils.printTicks(state);

        do {
            commandsExecutor.prepare();

            state = commandsExecutor.execute();

        } while (!state.isGameOver());

        PrintUtils.printGameOver(state);
        PrintUtils.printProducts(state);

        PrintUtils.printCommands(stateHolder);
        PrintUtils.printCustomers(stateHolder);

    }

    private CurrentTickRequest createExampleTickRequest(CurrentWorldResponse currentWorldResponse) {
        var request = new CurrentTickRequest();
        List<HireEmployeeCommand> hireEmployeeCommands = new ArrayList<>();
        request.setHireEmployeeCommands(hireEmployeeCommands);

        // Смотрим на каких кассах нет кассира (либо не был назначен, либо ушел с кассы отдыхать), нанимаем новых кассиров и ставим на эти кассы.
        // Нанимаем самых опытных!
        currentWorldResponse.getCheckoutLines().stream().filter(line -> line.getEmployeeId() == null).forEach(line -> {
            HireEmployeeCommand hireEmployeeCommand = new HireEmployeeCommand();
            hireEmployeeCommand.setCheckoutLineId(line.getId());
            hireEmployeeCommand.setExperience(HireEmployeeCommand.ExperienceEnum.SENIOR);
            hireEmployeeCommands.add(hireEmployeeCommand);
        });
        request.setHireEmployeeCommands(hireEmployeeCommands);

        // готовимся закупать товар на склад и выставлять его на полки
        ArrayList<BuyStockCommand> buyStockCommands = new ArrayList<>();
        request.setBuyStockCommands(buyStockCommands);

        ArrayList<PutOnRackCellCommand> putOnRackCellCommands = new ArrayList<>();
        request.setPutOnRackCellCommands(putOnRackCellCommands);

        List<Product> stock = currentWorldResponse.getStock();
        List<RackCell> rackCells = currentWorldResponse.getRackCells();

        // Обходим торговый зал и смотрим какие полки пустые. Выставляем на них товар.
        currentWorldResponse.getRackCells().stream().filter(rack -> rack.getProductId() == null || rack.getProductQuantity().equals(0)).forEach(rack -> {
            Product producttoPutOnRack = null;
            if (rack.getProductId() == null) {
                List<Integer> productsOnRack = rackCells.stream().filter(r -> r.getProductId() != null).map(RackCell::getProductId).collect(Collectors.toList());
                productsOnRack.addAll(putOnRackCellCommands.stream().map(c -> c.getProductId()).collect(Collectors.toList()));
                producttoPutOnRack = stock.stream().filter(product -> !productsOnRack.contains(product.getId())).findFirst().orElse(null);
            } else {
                producttoPutOnRack = stock.stream().filter(product -> product.getId().equals(rack.getProductId())).findFirst().orElse(null);
            }

            Integer productQuantity = rack.getProductQuantity();
            if (productQuantity == null) {
                productQuantity = 0;
            }

            // Вначале закупим товар на склад. Каждый ход закупать товар накладно, но ведь это тестовый игрок.
            Integer orderQuantity = rack.getCapacity() - productQuantity;
            if (producttoPutOnRack.getInStock() < orderQuantity) {
                BuyStockCommand command = new BuyStockCommand();
                command.setProductId(producttoPutOnRack.getId());
                command.setQuantity(100);
                buyStockCommands.add(command);
            }

            // Далее разложим на полки. И сформируем цену. Накинем 10 рублей к оптовой цене
            PutOnRackCellCommand command = new PutOnRackCellCommand();
            command.setProductId(producttoPutOnRack.getId());
            command.setRackCellId(rack.getId());
            command.setProductQuantity(orderQuantity);
            if (producttoPutOnRack.getSellPrice() == null) {
                command.setSellPrice(producttoPutOnRack.getStockPrice() + 10);
            }
            putOnRackCellCommands.add(command);

        });

        return request;
    }
}
