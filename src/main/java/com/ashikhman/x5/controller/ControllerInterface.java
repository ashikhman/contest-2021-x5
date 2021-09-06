package com.ashikhman.x5.controller;

import com.ashikhman.x5.command.CommandInterface;

import java.util.List;

public interface ControllerInterface {

    List<CommandInterface> execute();

    default boolean isEnabled() {
        return true;
    }
}
