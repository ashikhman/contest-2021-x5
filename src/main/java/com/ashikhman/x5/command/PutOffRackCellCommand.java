package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.model.RackCellModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PutOffRackCellCommand implements CommandInterface {

    private RackCellModel cell;

    @Override
    public void updateRequest(CurrentTickRequest request) {
        var command = new com.ashikhman.x5.client.api.model.PutOffRackCellCommand();
        command.setRackCellId(cell.getId());

        request.addPutOffRackCellCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("off,%s", cell.getId());
    }
}
