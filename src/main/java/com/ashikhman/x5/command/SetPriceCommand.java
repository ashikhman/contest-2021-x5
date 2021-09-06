package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.entity.ProductEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SetPriceCommand implements CommandInterface {

    private ProductEntity product;

    private double price;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new com.ashikhman.x5.client.api.model.SetPriceCommand();
        command.setProductId(product.getId());
        command.setSellPrice(price);

        request.addSetPriceCommandsItem(command);
    }

    public String toString() {
        return String.format("price,%s,%s", product.getId(), price);
    }
}
