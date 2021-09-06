package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.ApiException;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final PerfectStoreEndpointApi client;

    public CurrentWorldResponse tick(CurrentTickRequest request) {
        try {
            return client.tick(request);
        } catch (ApiException e) {
            throw new UnexpectedException("Tick request returned an error", e);
        }
    }

    public CurrentWorldResponse loadWord() {
        var awaitTimes = 60;
        var count = 0;
        var serverReady = false;
        do {
            try {
                count += 1;

                return client.loadWorld();
            } catch (ApiException e) {
                //log.info("Failed to load world data", e);
                try {
                    Thread.currentThread().sleep(1000L);
                } catch (InterruptedException interruptedException) {
                    //interruptedException.printStackTrace();
                }
            }
        } while (!serverReady && count < awaitTimes);

        throw new UnexpectedException(String.format("Game server did not responded in %s seconds", awaitTimes));
    }
}
