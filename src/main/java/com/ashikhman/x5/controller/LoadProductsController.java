package com.ashikhman.x5.controller;

import com.ashikhman.x5.command.CommandInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Order(0)
public class LoadProductsController extends AbstractController {

    @Override
    public List<CommandInterface> execute() {
        return new ArrayList<>();
    }
}
