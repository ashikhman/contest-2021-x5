package com.ashikhman.x5.controller;

import com.ashikhman.x5.entity.StateEntity;
import com.ashikhman.x5.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController implements ControllerInterface {

    private StateRepository stateRepository;

    @Autowired
    public final void setStateRepository(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    protected StateEntity getState() {
        return stateRepository.get();
    }
}
