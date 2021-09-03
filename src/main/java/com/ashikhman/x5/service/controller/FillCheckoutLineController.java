package com.ashikhman.x5.service.controller;

import com.ashikhman.x5.client.api.model.HireEmployeeCommand;
import com.ashikhman.x5.command.CommandInterface;
import com.ashikhman.x5.command.HireEmployeeToLineCommand;
import com.ashikhman.x5.command.SetEmployeeToLineCommand;
import com.ashikhman.x5.model.CheckoutLineModel;
import com.ashikhman.x5.model.EmployeeModel;
import com.ashikhman.x5.model.GameStateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FillCheckoutLineController implements ControllerInterface {

    @Override
    public List<CommandInterface> execute(GameStateModel state) {
        var freeEmployees = new ArrayList<>(state.getEmployees().values())
                .stream()
                .filter(this::isFreeEmployee)
                .sorted(Comparator.comparingInt(EmployeeModel::getExperience))
                .collect(Collectors.toList());

        var commands = new ArrayList<CommandInterface>();
        for (var entry : state.getCheckoutLines().entrySet()) {
            var line = entry.getValue();
            if (null != line.getEmployee()) {
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

    private boolean isFreeEmployee(EmployeeModel employee) {
        if (null != employee.getCheckoutLine()) {
            return false;
        }
        if (employee.getVacationTicks() < 960) {
            return false;
        }

        return true;
    }

    private CommandInterface setEmployee(CheckoutLineModel line, EmployeeModel employee) {
        return new SetEmployeeToLineCommand()
                .setLine(line)
                .setEmployee(employee);
    }

    private CommandInterface hireEmployee(CheckoutLineModel line) {
        return new HireEmployeeToLineCommand()
                .setLine(line)
                .setExperience(HireEmployeeCommand.ExperienceEnum.SENIOR);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
