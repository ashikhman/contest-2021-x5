package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.entity.CheckoutLineEntity;
import com.ashikhman.x5.entity.CustomerEntity;
import com.ashikhman.x5.entity.EmployeeEntity;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutLineRepository implements RepositoryInterface {

    private final Map<Integer, CheckoutLineEntity> entities = new HashMap<>();

    private final EmployeeRepository employeeRepository;

    private final CustomerRepository customerRepository;

    public CheckoutLineEntity getById(Integer id) {
        var entity = entities.get(id);
        if (null == entity) {
            throw new UnexpectedException(String.format("Checkout line with id `%s` not found", id));
        }

        return entity;
    }

    public List<CheckoutLineEntity> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Nullable
    public CheckoutLineEntity getByEmployee(EmployeeEntity employee) {
        for (var entry : entities.entrySet()) {
            var line = entry.getValue();
            if (line.getEmployeeId() != null && line.getEmployeeId().equals(employee.getId())) {
                return line;
            }
        }

        return null;
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        worldResponse.getCheckoutLines().forEach(lineResponse -> {
            var line = getById(lineResponse.getId())
                    .setEmployeeId(lineResponse.getEmployeeId())
                    .setCustomerId(lineResponse.getCustomerId());

            updateEmployee(line);
            updateCustomer(line);
        });
    }

    private void updateEmployee(CheckoutLineEntity line) {
        line.setEmployee(null);
        if (null != line.getEmployeeId()) {
            var employee = employeeRepository.getById(line.getEmployeeId());
            line.setEmployee(employee);
            employee.setCheckoutLine(line);
        }
    }

    private void updateCustomer(CheckoutLineEntity line) {
        CustomerEntity customer = null;
        if (null != line.getCustomerId()) {
            customer = customerRepository.getById(line.getCustomerId());
        }
        line.setCustomer(customer);
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        worldResponse.getCheckoutLines().forEach(lineResponse -> {
            var entity = new CheckoutLineEntity()
                    .setId(lineResponse.getId());

            entities.put(entity.getId(), entity);
        });
    }

    @Override
    public List<RepositoryInterface> getDependencies() {
        return List.of(employeeRepository, customerRepository);
    }
}
