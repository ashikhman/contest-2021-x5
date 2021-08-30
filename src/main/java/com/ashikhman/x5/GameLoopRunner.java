package com.ashikhman.x5;

import com.ashikhman.x5.service.GameLoop;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameLoopRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final GameLoop loop;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loop.run();
    }
}
