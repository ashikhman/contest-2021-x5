package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.repository.RepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RepositoryLifecycleManager {

    private final Set<RepositoryInterface> repositories = new LinkedHashSet<>();

    public RepositoryLifecycleManager(List<RepositoryInterface> repositories) {
        repositories.forEach(this::addRepository);
    }

    public void init(CurrentWorldResponse response) {
        repositories.forEach(repository -> repository.init(response));
    }

    public void update(CurrentWorldResponse response) {
        repositories.forEach(repository -> repository.update(response));
    }

    public void postUpdate() {
        repositories.forEach(RepositoryInterface::postUpdate);
    }

    private void addRepository(RepositoryInterface repository) {
        for (var dependency : repository.getDependencies()) {
            if (!repositories.contains(dependency)) {
                addRepository(dependency);
            }
        }
        repositories.add(repository);
    }
}
