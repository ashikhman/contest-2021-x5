package com.ashikhman.x5.model;

import com.ashikhman.x5.client.api.model.Customer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CustomerModel {

    private int id;

    private Customer.ModeEnum mode;

    private List<ProductInBasketModel> products = new ArrayList<>();

    public CustomerModel addProduct(ProductInBasketModel product) {
        products.add(product);

        return this;
    }
}
