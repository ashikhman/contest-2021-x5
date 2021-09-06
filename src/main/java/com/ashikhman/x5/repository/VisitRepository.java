package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.client.api.model.Customer;
import com.ashikhman.x5.entity.VisitEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VisitRepository implements RepositoryInterface {

    private final List<VisitEntity> visits = new ArrayList<>(6000);

    private final StateRepository stateRepository;

    private Map<Integer, Customer> currentCustomers = new HashMap<>();

    public List<VisitEntity> getAll() {
        return Collections.unmodifiableList(visits);
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        var presentIds = new ArrayList<Integer>();
        worldResponse.getCustomers().forEach(customer -> {

        });

        currentCustomers.entrySet().removeIf(entry -> !presentIds.contains(entry.getKey()));
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
