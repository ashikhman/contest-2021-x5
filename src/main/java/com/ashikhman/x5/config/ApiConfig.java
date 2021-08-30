package com.ashikhman.x5.config;

import com.ashikhman.x5.client.api.ApiClient;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiConfig {

    private final AppProperties properties;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient().setBasePath(properties.getApiUrl());
    }

    @Bean
    public PerfectStoreEndpointApi perfectStoreEndpointApi(ApiClient client) {
        return new PerfectStoreEndpointApi(client);
    }
}
