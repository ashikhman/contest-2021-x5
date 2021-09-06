package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.entity.ProductEntity;
import com.ashikhman.x5.entity.RackCellEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PutOnRackCellCommand implements CommandInterface {

    private RackCellEntity cell;

    private ProductEntity product;

    private double sellPrice;

    private int quantity;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new com.ashikhman.x5.client.api.model.PutOnRackCellCommand();
        command.setRackCellId(cell.getId());
        command.setProductId(product.getId());
        command.setProductQuantity(quantity);
        command.setSellPrice(sellPrice);

        request.addPutOnRackCellCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("on,%s,%s,%s,%s", cell.getId(), product.getId(), quantity, sellPrice);
    }
}
