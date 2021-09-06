package com.ashikhman.x5.event;

import com.ashikhman.x5.entity.CheckoutLineEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UnemployedCheckoutLineEvent implements EventInterface {

    private CheckoutLineEntity line;

    @Override
    public String toString() {
        return String.format("ECL:%s", line.getId());
    }
}
