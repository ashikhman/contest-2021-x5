package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.client.api.model.Employee;
import com.ashikhman.x5.entity.EmployeeEntity;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeRepository implements RepositoryInterface {

    private final Map<Integer, EmployeeEntity> entities = new HashMap<>();

    public EmployeeEntity getById(Integer id) {
        var entity = entities.get(id);
        if (null == entity) {
            throw new UnexpectedException(String.format("Employee with id `%s` not found", id));
        }

        return entity;
    }

    public List<EmployeeEntity> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        worldResponse.getEmployees().forEach(employeeResponse ->
                insert(employeeResponse)
                        .setLastName(employeeResponse.getFirstName())
                        .setLastName(employeeResponse.getLastName())
                        .setExperience(employeeResponse.getExperience())
                        .setSalary(employeeResponse.getSalary())
                        .setCheckoutLine(null)
        );
    }

    @Override
    public void postUpdate() {
        for (var entry : entities.entrySet()) {
            var employee = entry.getValue();
            if (null == employee.getCheckoutLine()) {
                employee.setVacationTicks(employee.getVacationTicks() + 1);
                employee.setWorkingTicks(1);
            } else {
                employee.setWorkingTicks(employee.getWorkingTicks() + 1);
                employee.setVacationTicks(1);
            }
        }
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        worldResponse.getEmployees().forEach(this::insert);
    }

    private EmployeeEntity insert(Employee employeeResponse) {
        var entity = entities.get(employeeResponse.getId());
        if (null == entity) {
            entity = new EmployeeEntity().setId(employeeResponse.getId());
            entities.put(entity.getId(), entity);
        }

        return entity;
    }
}
