package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.SetOnCheckoutLineCommand;
import com.ashikhman.x5.model.CheckoutLineModel;
import com.ashikhman.x5.model.EmployeeModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SetEmployeeToLineCommand implements CommandInterface {

    private EmployeeModel employee;

    private CheckoutLineModel line;

    @Override
    public void updateRequest(CurrentTickRequest request) {
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
