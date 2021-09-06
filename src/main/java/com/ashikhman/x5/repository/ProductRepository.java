package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.entity.ProductEntity;
import com.ashikhman.x5.exception.UnexpectedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductRepository implements RepositoryInterface {

    private final Map<Integer, ProductEntity> entities = new HashMap<>();

    public ProductEntity getById(Integer id) {
        var entity = entities.get(id);
        if (null == entity) {
            throw new UnexpectedException(String.format("Product with id `%s` not found", id));
        }

        return entity;
    }

    public void printAll() {
        var joiner = new StringJoiner("|");
        entities.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .forEach(product -> joiner.add(String.format(
                        "%s,%s,%s",
                        product.getId(),
                        product.getName(),
                        product.getStockPrice()
                )));

        System.out.printf("PRODUCTS: %s@\n", joiner);
    }

    public List<ProductEntity> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        worldResponse.getStock().forEach(productResponse -> {
            var sellPrice = productResponse.getSellPrice();
            if (null == sellPrice) {
                sellPrice = 0.0;
            }

            getById(productResponse.getId())
                    .setQuantity(productResponse.getInStock())
                    .setSellPrice(sellPrice);
        });
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        worldResponse.getStock().forEach(productResponse -> {
            var entity = new ProductEntity()
                    .setId(productResponse.getId())
                    .setName((productResponse.getName()))
                    .setStockPrice(productResponse.getStockPrice())
                    .setTotalQuantity(0);

            entities.put(entity.getId(), entity);
        });
    }
}
