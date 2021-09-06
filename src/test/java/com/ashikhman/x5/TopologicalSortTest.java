package com.ashikhman.x5;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TopologicalSortTest {

    private final Set<Repository> sortedRepositories = new LinkedHashSet<>();

    @Test
    void xaxa() {
        var stateRepository = new Repository("state", List.of());
        var customerRepository = new Repository("customer", List.of());
        var productRepository = new Repository("product", List.of());
        var visitRepository = new Repository("visit", List.of(stateRepository, customerRepository));
        var grabRepository = new Repository("grab", List.of(visitRepository, productRepository));
        var allRepository = new Repository("all", List.of(stateRepository, customerRepository, productRepository, visitRepository, grabRepository));

        var repositories = new ArrayList<>(List.of(
                stateRepository,
                customerRepository,
                productRepository,
                visitRepository,
                grabRepository,
                allRepository
        ));
        Collections.shuffle(repositories);
        for (var repository : repositories) {
            topologicalSort(repository);
        }

        sortedRepositories.forEach(repository -> System.out.println(repository.name));
    }

    private void topologicalSort(Repository repository) {
        for (var dependency : repository.dependsOn) {
            if (!sortedRepositories.contains(dependency)) {
                topologicalSort(dependency);
            }
        }
        sortedRepositories.add(repository);
    }

    private static class Repository {

        public final String name;

        public final List<Repository> dependsOn;

        public Repository(String name, List<Repository> dependsOn) {
            this.name = name;
            this.dependsOn = Collections.unmodifiableList(dependsOn);
        }
    }
}
