package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.client.api.model.Customer;
import com.ashikhman.x5.entity.BasketItemEntity;
import com.ashikhman.x5.entity.CustomerEntity;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerRepository implements RepositoryInterface {

    private final Map<Integer, CustomerEntity> entities = new HashMap<>();

    private final List<CustomerEntity> deletedEntities = new ArrayList<>();

    private final StateRepository stateRepository;

    private final ProductRepository productRepository;

    public CustomerEntity getById(Integer id) {
        var entity = entities.get(id);
        if (null == entity) {
            throw new UnexpectedException(String.format("Customer with id `%s` not found", id));
        }

        return entity;
    }

    public List<CustomerEntity> getDeleted() {
        return List.copyOf(deletedEntities);
    }

    public Map<Integer, CustomerEntity> getMap() {
        return Collections.unmodifiableMap(entities);
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        var state = stateRepository.get();
        var presentIds = new ArrayList<Integer>();
        worldResponse.getCustomers().forEach(customerResponse -> {
            var customer = insert(customerResponse)
                    .setMode(customerResponse.getMode())
                    .setAppearedAt(state.getCurrentTick());

            updateBasket(customerResponse, customer);

            if (presentIds.contains(customer.getId())) {
                System.out.println("Duplicate customer detected");
            }
            presentIds.add(customer.getId());
        });

        deletedEntities.clear();
        entities.entrySet().removeIf(entry -> {
            if (!presentIds.contains(entry.getKey())) {
                entry.getValue().setDisappearedAt(state.getCurrentTick());
                deletedEntities.add(entry.getValue());

                return true;
            }

            return false;
        });
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        worldResponse.getCustomers().forEach(this::insert);
    }

    private void updateBasket(Customer customerResponse, CustomerEntity customerEntity) {
        customerResponse.getBasket().forEach(itemResponse -> {
            var itemEntity = customerEntity.getBasket().get(itemResponse.getId());
            if (null == itemEntity) {
                itemEntity = new BasketItemEntity()
                        .setProduct(productRepository.getById(itemResponse.getId()));
                customerEntity.getBasket().put(itemResponse.getId(), itemEntity);
            } else {
                if (itemResponse.getProductCount() < itemEntity.getCount()) {
                    System.out.println("Product count has decreased in customer's basket");
                }
                if (!itemResponse.getPrie().equals(itemEntity.getPrice())) {
                    System.out.println("Product price has changed in customer's basket");
                }
            }

            itemEntity
                    .setCount(itemResponse.getProductCount())
                    .setPrice(itemResponse.getPrie());
        });
    }

    private CustomerEntity insert(Customer customerResponse) {
        var entity = entities.get(customerResponse.getId());
        if (null == entity) {
            entity = new CustomerEntity().setId(customerResponse.getId());
            entities.put(entity.getId(), entity);
        }

        return entity;
    }

    @Override
    public List<RepositoryInterface> getDependencies() {
        return List.of(stateRepository, productRepository);
    }
}
