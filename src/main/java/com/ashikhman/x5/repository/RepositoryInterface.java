package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;

import java.util.List;

public interface RepositoryInterface {

    default void update(CurrentWorldResponse worldResponse) {
    }

    default void init(CurrentWorldResponse worldResponse) {
    }

    default void postUpdate() {
    }

    default boolean isEnabled() {
        return true;
    }

    default List<RepositoryInterface> getDependencies() {
        return List.of();
    }
}
