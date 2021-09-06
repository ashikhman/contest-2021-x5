package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.client.api.model.Customer;
import com.ashikhman.x5.client.api.model.ProductInBasket;
import com.ashikhman.x5.entity.GrabEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GrabRepository implements RepositoryInterface {

    private Map<Integer, Customer> currentCustomers = new HashMap<>();

    private List<GrabEntity> grabs = new ArrayList<>(100000);

    public void printStats() {
        var totalPrice = 0.0;
        for (var grab : grabs) {
            totalPrice += grab.getPrice();
        }

        System.out.printf("Grabs count: %s\n", grabs.size());
        System.out.printf("Grabs price: %.2f\n", totalPrice);
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        var presentIds = new ArrayList<Integer>();
        worldResponse.getCustomers().forEach(customer -> {
            var prevCustomer = currentCustomers.get(customer.getId());
            if (null == prevCustomer) {
                addGrabs(customer, worldResponse.getCurrentTick());
            } else {
                addGrabs(customer, prevCustomer, worldResponse.getCurrentTick());
            }

            currentCustomers.put(customer.getId(), customer);

            presentIds.add(customer.getId());
        });

        currentCustomers.entrySet().removeIf(entry -> !presentIds.contains(entry.getKey()));
    }

    @Nullable
    private ProductInBasket getProductInBasket(List<ProductInBasket> basket, Integer id) {
        for (var item : basket) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        return null;
    }

    private void addGrabs(Customer customer, Customer prevCustomer, int currentTick) {
        var presentProductIds = new ArrayList<Integer>();
        for (var item : customer.getBasket()) {
            var prevItem = getProductInBasket(prevCustomer.getBasket(), item.getId());
            if (null == prevItem) {
                addGrab(item, customer, currentTick);
            } else {
                if (item.getProductCount() < prevItem.getProductCount()) {
                    System.out.printf("Customer has decreased count of a product in the basket: %s %s\n", customer.getId(), item.getId());
                }
                if (!item.getPrie().equals(prevItem.getPrie())) {
                    System.out.printf("Product price in basket is changed: old - %s, new - %s\n", prevItem.getPrie(), item.getPrie());
                }

                var diff = Math.abs(item.getProductCount() - prevItem.getProductCount());
                for (var i = 0; i < diff; i++) {
                    var grab = new GrabEntity()
                            .setProductId(item.getId())
                            .setCustomerId(customer.getId())
                            .setPrice(item.getPrie())
                            .setTick(currentTick);

                    grabs.add(grab);
                }
            }

            if (presentProductIds.contains(item.getId())) {
                System.out.println("Basket contains multiple items with same product id");
            }

            presentProductIds.add(item.getId());
        }

        prevCustomer.getBasket().forEach(item -> {
            if (!presentProductIds.contains(item.getId())) {
                System.out.println("Customer has completely removed a product from the basket");
            }
        });
    }

    private void addGrabs(Customer customer, int currentTick) {
        for (var item : customer.getBasket()) {
            addGrab(item, customer, currentTick);
        }
    }

    private void addGrab(ProductInBasket item, Customer customer, int currentTick) {
        for (var i = 0; i < item.getProductCount(); i++) {
            var grab = new GrabEntity()
                    .setProductId(item.getId())
                    .setCustomerId(customer.getId())
                    .setPrice(item.getPrie())
                    .setTick(currentTick);

            grabs.add(grab);
        }
    }
}
