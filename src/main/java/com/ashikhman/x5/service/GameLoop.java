package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.ApiException;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameLoop {

    private final PerfectStoreEndpointApi storeApi;

    private final GameStateHolder stateHolder;

    public void run() {
        log.info("Running game loop");

        var state = stateHolder.getState();
        log.info("Total ticks count: {}", state.getTickCount());

        do {
            var tickRequest = new CurrentTickRequest();
            tickRequest.setHireEmployeeCommands(new ArrayList<>());
            tickRequest.setBuyStockCommands(new ArrayList<>());
            tickRequest.setPutOnRackCellCommands(new ArrayList<>());

            CurrentWorldResponse worldResponse;
            try {
                worldResponse = storeApi.tick(tickRequest);
            } catch (ApiException e) {
                throw new UnexpectedException("Failed to send new tick to the API", e);
            }

            state = stateHolder.update(worldResponse);

        } while (!state.isGameOver());

        log.info("Game is over");
    }
}
