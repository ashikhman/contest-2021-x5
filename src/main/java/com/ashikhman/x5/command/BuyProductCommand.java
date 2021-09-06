package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.BuyStockCommand;
import com.ashikhman.x5.client.api.model.CurrentTickRequest;
import com.ashikhman.x5.entity.ProductEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BuyProductCommand implements CommandInterface {

    private ProductEntity product;

    private int quantity;

    @Override
    public void execute(CurrentTickRequest request) {
        var command = new BuyStockCommand();
        command.setProductId(product.getId());
        command.setQuantity(quantity);

        request.addBuyStockCommandsItem(command);

        product.setQuantity(product.getQuantity() + quantity);
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
    }

    @Override
    public String toString() {
        return String.format("buy,%s,%s", product.getId(), quantity);
    }
}
