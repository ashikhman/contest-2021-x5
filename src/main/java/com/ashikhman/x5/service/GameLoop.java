package com.ashikhman.x5.service;

import com.ashikhman.x5.repository.RackCellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameLoop {

    private final RepositoryLifecycleManager lifecycleManager;

    private final CommandsExecutor commandsExecutor;

    private final ApiService apiService;

    private final EventManager eventManager;

    private final ResultPrinter resultPrinter;

    private final RackCellRepository rackCellRepository;

    public void run() {
        var state = apiService.loadWord();
        lifecycleManager.init(state);

        do {
            lifecycleManager.update(state);
            lifecycleManager.postUpdate();

            eventManager.execute();

            state = apiService.tick(commandsExecutor.execute());
        } while (!state.isGameOver());

        System.out.println("GAME OVER");
        resultPrinter.print();
        rackCellRepository.printAll();
    }
}
