package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.client.api.model.HireEmployeeCommand;
import com.ashikhman.x5.model.CheckoutLineModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HireEmployeeToLineCommand implements CommandInterface {

    private CheckoutLineModel line;

    private HireEmployeeCommand.ExperienceEnum experience;

    @Override
    public void updateRequest(CurrentTickRequest request) {
        var command = new HireEmployeeCommand();
        command.setExperience(experience);
        command.setCheckoutLineId(line.getId());

        request.addHireEmployeeCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("hire,%s,%s", line.getId(), experience.toString());
    }
}
