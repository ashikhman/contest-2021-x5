package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.ApiException;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.exception.UnexpectedException;
import com.ashikhman.x5.model.GameStateModel;
import com.ashikhman.x5.service.controller.ControllerInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandsExecutor {

    private final GameStateHolder stateHolder;

    private final PerfectStoreEndpointApi api;

    private final List<ControllerInterface> controllers;

    public void prepare() {
        controllers.sort(Comparator.comparingInt(ControllerInterface::getOrder));

        var state = stateHolder.getState();

        controllers.forEach(controller -> {
            var newCommands = controller.execute(state);
            state.getCommands().addAll(newCommands);
        });
    }

    public GameStateModel execute() {
        var state = stateHolder.getState();

        var request = new CurrentTickRequest();
        request.setSetOnCheckoutLineCommands(new ArrayList<>());
        request.setOffCheckoutLineCommands(new ArrayList<>());
        request.setHireEmployeeCommands(new ArrayList<>());
        request.setFireEmployeeCommands(new ArrayList<>());
        request.setBuyStockCommands(new ArrayList<>());
        request.setPutOnRackCellCommands(new ArrayList<>());
        request.setPutOffRackCellCommands(new ArrayList<>());

        state.getCommands()
                .forEach(command -> command.updateRequest(request));

        if (!state.getCommands().isEmpty() || state.getCurrentTick() % 60 == 0) {
            stateHolder.saveHistory();
        }

        try {
            return stateHolder.update(api.tick(request));
        } catch (ApiException e) {
            throw new UnexpectedException("Error occured while sending tick request", e);
        }
    }
}
