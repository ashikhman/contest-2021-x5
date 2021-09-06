package com.ashikhman.x5.controller;

import com.ashikhman.x5.client.api.model.HireEmployeeCommand;
import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.command.HireEmployeeToLineCommand;
import com.ashikhman.x5.command.SetEmployeeToLineCommand;
import com.ashikhman.x5.entity.CheckoutLineEntity;
import com.ashikhman.x5.entity.EmployeeEntity;
import com.ashikhman.x5.repository.CheckoutLineRepository;
import com.ashikhman.x5.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class CheckoutLineController extends AbstractController {

    private final EmployeeRepository employeeRepository;

    private final CheckoutLineRepository checkoutLineRepository;

    @Override
    public List<CommandInterface> execute() {
        var commands = new ArrayList<CommandInterface>();
        var freeEmployees = employeeRepository.getAll()
                .stream()
                .filter(this::isFreeEmployee)
                .sorted(Comparator.comparingInt(EmployeeEntity::getExperience))
                .collect(Collectors.toList());


        for (var line : checkoutLineRepository.getAll()) {
            if (null != line.getEmployeeId()) {
                continue;
            }

            if (freeEmployees.size() > 0) {
                var employee = freeEmployees.remove(freeEmployees.size() - 1);
                commands.add(setEmployee(line, employee));
            } else {
                commands.add(hireEmployee(line));
            }
        }

        return commands;
    }

    private boolean isFreeEmployee(EmployeeEntity employee) {
        var line = checkoutLineRepository.getByEmployee(employee);
        if (null != line) {
            return false;
        }
        if (employee.getVacationTicks() < 960) {
            return false;
        }

        return true;
    }

    private CommandInterface setEmployee(CheckoutLineEntity line, EmployeeEntity employee) {
        return new SetEmployeeToLineCommand()
                .setLine(line)
                .setEmployee(employee);
    }

    private CommandInterface hireEmployee(CheckoutLineEntity line) {
        return new HireEmployeeToLineCommand()
                .setLine(line)
                .setExperience(HireEmployeeCommand.ExperienceEnum.SENIOR);
    }
}
