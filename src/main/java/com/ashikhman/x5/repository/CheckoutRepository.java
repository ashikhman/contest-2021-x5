package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.entity.CheckoutEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckoutRepository implements RepositoryInterface {

    private final CheckoutLineRepository checkoutLineRepository;

    private final StateRepository stateRepository;

    private final Map<Integer, Integer> customerOnLines = new HashMap<>();

    private final List<CheckoutEntity> checkouts = new ArrayList<>();

    public List<CheckoutEntity> getAll() {
        return Collections.unmodifiableList(checkouts);
    }

    @Override
    public void postUpdate() {
        var state = stateRepository.get();
        checkoutLineRepository.getAll().forEach(line -> {
            var prevCustomerId = customerOnLines.get(line.getId());
            if (null != line.getCustomerId() && !line.getCustomerId().equals(prevCustomerId)) {
                var basketPrice = 0.0;
                for (var product : line.getCustomer().getBasket().values()) {
                    basketPrice += product.getPrice() * product.getCount();
                }

                var checkout = new CheckoutEntity()
                        .setLineId(line.getId())
                        .setCustomerId(line.getCustomerId())
                        .setStartTick(state.getCurrentTick())
                        .setBasketPrice(basketPrice);

                checkouts.add(checkout);
            }

            customerOnLines.put(line.getId(), line.getCustomerId());
        });
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        checkoutLineRepository.getAll().forEach(line -> customerOnLines.put(line.getId(), null));
    }

    @Override
    public List<RepositoryInterface> getDependencies() {
        return List.of(stateRepository, checkoutLineRepository);
    }
}
