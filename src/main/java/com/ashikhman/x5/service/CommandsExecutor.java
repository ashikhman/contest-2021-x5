package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.controller.ControllerInterface;
import com.ashikhman.x5.repository.StateRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandsExecutor {

    private final List<ControllerInterface> controllers;

    private final StateRepository stateRepository;

    @Getter
    private final Map<Integer, List<CommandInterface>> history = new HashMap<>();

    public CommandsExecutor(List<ControllerInterface> controllers, StateRepository stateRepository) {
        this.controllers = controllers.stream()
                .filter(ControllerInterface::isEnabled)
                .collect(Collectors.toList());
        this.stateRepository = stateRepository;
    }

    public CurrentTickRequest execute() {
        var request = new CurrentTickRequest();
        request.setSetOnCheckoutLineCommands(new ArrayList<>());
        request.setOffCheckoutLineCommands(new ArrayList<>());
        request.setHireEmployeeCommands(new ArrayList<>());
        request.setFireEmployeeCommands(new ArrayList<>());
        request.setBuyStockCommands(new ArrayList<>());
        request.setPutOnRackCellCommands(new ArrayList<>());
        request.setPutOffRackCellCommands(new ArrayList<>());
        request.setPriceCommands(new ArrayList<>());

        var executedCommands = new ArrayList<CommandInterface>();
        controllers.forEach(controller -> {
            for (var command : controller.execute()) {
                command.execute(request);
                executedCommands.add(command);
            }
        });

        if (!executedCommands.isEmpty()) {
            history.put(stateRepository.get().getCurrentTick(), executedCommands);
        }

        return request;
    }

    public void printAll() {
        var output = new StringBuilder();
        output.append("COMMANDS: ");

        history.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    output.append(String.format("[%s]", entry.getKey()));

                    var joiner = new StringJoiner(";");
                    for (var command : entry.getValue()) {
                        joiner.add(command.toString());
                    }
                    output.append(joiner);
                });
        output.append("@");

        System.out.println(output);
    }
}
