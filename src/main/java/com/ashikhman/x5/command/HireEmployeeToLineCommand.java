package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.HireEmployeeCommand;
import com.ashikhman.x5.entity.CheckoutLineEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HireEmployeeToLineCommand implements CommandInterface {

    private CheckoutLineEntity line;

    private HireEmployeeCommand.ExperienceEnum experience;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new HireEmployeeCommand();
        command.setExperience(experience);
        command.setCheckoutLineId(line.getId());

        request.addHireEmployeeCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("hire,%s,%s", line.getId(), experience);
    }
}
