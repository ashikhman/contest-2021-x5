package com.ashikhman.x5.service.controller;

import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.model.GameStateModel;

import java.util.List;

public interface ControllerInterface {

    List<CommandInterface> execute(GameStateModel state);

    int getOrder();
}
