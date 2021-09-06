package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.entity.StateEntity;
import org.springframework.stereotype.Service;

@Service
public class StateRepository implements RepositoryInterface {

    private final StateEntity entity = new StateEntity();

    public StateEntity get() {
        return entity;
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        entity
                .setTickCount(worldResponse.getTickCount())
                .setCurrentTick(worldResponse.getCurrentTick())
                .setIncome(worldResponse.getIncome())
                .setSalaryCosts(worldResponse.getSalaryCosts())
                .setStockCosts(worldResponse.getStockCosts())
                .setGameOver(worldResponse.isGameOver());
    }
}
