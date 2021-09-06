package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.entity.RackCellEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PutOffRackCellCommand implements CommandInterface {

    private RackCellEntity cell;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new com.ashikhman.x5.client.api.model.PutOffRackCellCommand();
        command.setRackCellId(cell.getId());

        request.addPutOffRackCellCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("off,%s", cell.getId());
    }
}
