package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.model.ProductModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BuyStockCommand implements CommandInterface {

    private ProductModel product;

    private int quantity;

    @Override
    public void updateRequest(CurrentTickRequest request) {
        var command = new com.ashikhman.x5.client.api.model.BuyStockCommand();
        command.setProductId(product.getId());
        command.setQuantity(quantity);

        request.addBuyStockCommandsItem(command);
    }

    @Override
    public String toString() {
        return String.format("buy,%s,%s", product.getId(), quantity);
    }
}
