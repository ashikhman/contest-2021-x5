package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.SetOnCheckoutLineCommand;
import com.ashikhman.x5.entity.CheckoutLineEntity;
import com.ashikhman.x5.entity.EmployeeEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SetEmployeeToLineCommand implements CommandInterface {

    private EmployeeEntity employee;

    private CheckoutLineEntity line;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new SetOnCheckoutLineCommand();
        command.setEmployeeId(employee.getId());
        command.setCheckoutLineId(line.getId());

        request.addSetOnCheckoutLineCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("setEmp,%s,%s", line.getId(), employee.getId());
    }
}
