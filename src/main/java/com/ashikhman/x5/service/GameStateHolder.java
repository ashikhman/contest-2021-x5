package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.ApiException;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.exception.UnexpectedException;
import com.ashikhman.x5.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameStateHolder {

    private final PerfectStoreEndpointApi api;

    private boolean stateLoaded = false;

    private GameStateModel state = new GameStateModel();

    public GameStateModel getState() {
        if (!stateLoaded) {
            synchronized (this) {
                if (!stateLoaded) {
                    state = load();
                }
            }
        }

        return state;
    }

    public GameStateModel update(CurrentWorldResponse worldResponse) {
        state.setTickCount(worldResponse.getTickCount())
                .setCurrentTick(worldResponse.getCurrentTick())
                .setIncome(worldResponse.getIncome())
                .setSalaryCosts(worldResponse.getSalaryCosts())
                .setStockCosts(worldResponse.getStockCosts())
                .setGameOver(worldResponse.isGameOver());

        updateProducts(worldResponse);
        updateEmployees(worldResponse);
        updateCustomer(worldResponse);
        updateCheckoutLines(worldResponse);
        updateRecruitmentOffers(worldResponse);
        updateRackCells(worldResponse);

        return state;
    }

    private void updateRackCells(CurrentWorldResponse worldResponse) {
        var presentCells = new HashSet<Integer>();
        worldResponse.getRackCells().forEach(cellResponse -> {
            ProductModel product = null;
            if (null != cellResponse.getProductId()) {
                product = state.getProducts().get(cellResponse.getProductId());
                if (null == product) {
                    log.error("No product found for id: {}", cellResponse.getId());
                }
            }

            var cellModel = state.getRackCells()
                    .putIfAbsent(
                            cellResponse.getId(),
                            new RackCellModel().setId(cellResponse.getId())
                    )
                    .setVisibility(cellResponse.getVisibility())
                    .setCapacity(cellResponse.getCapacity())
                    .setProduct(product)
                    .setProductName(cellResponse.getProductName())
                    .setProductQuantity(cellResponse.getProductQuantity());

            presentCells.add(cellModel.getId());
        });

        state.getRackCells().forEach((id, rackCellModel) -> {
            if (!presentCells.contains(id)) {
                state.getRackCells().remove(id);
            }
        });
    }

    private void updateRecruitmentOffers(CurrentWorldResponse worldResponse) {
        state.getRecruitmentOffers().clear();
        worldResponse.getRecruitmentAgency().forEach(offerResponse -> state.getRecruitmentOffers().add(
                new RecruitmentOfferModel()
                        .setExperience(offerResponse.getExperience())
                        .setEmployeeType(offerResponse.getEmployeeType())
                        .setSalary(offerResponse.getSalary())
        ));

    }

    private void updateCheckoutLines(CurrentWorldResponse worldResponse) {
        var presentLines = new HashSet<Integer>();
        worldResponse.getCheckoutLines().forEach(checkoutLineResponse -> {
            var customer = state.getCustomers().get(checkoutLineResponse.getCustomerId());
            if (null == customer) {
                log.error("No customer found for id: {}", checkoutLineResponse.getCustomerId());
                return;
            }

            var employee = state.getEmployees().get(checkoutLineResponse.getEmployeeId());
            if (null == employee) {
                log.error("No employee found for id: {}", checkoutLineResponse.getEmployeeId());
                return;
            }

            var checkoutLineModel = state.getCheckoutLines()
                    .putIfAbsent(
                            checkoutLineResponse.getId(),
                            new CheckoutLineModel().setId(checkoutLineResponse.getId())
                    )
                    .setCustomer(customer)
                    .setEmployee(employee);

            presentLines.add(checkoutLineModel.getId());
        });

        state.getCheckoutLines().forEach((id, checkoutLineModel) -> {
            if (!presentLines.contains(id)) {
                state.getCheckoutLines().remove(id);
            }
        });
    }

    private void updateCustomer(CurrentWorldResponse worldResponse) {
        var presentCustomer = new HashSet<Integer>();
        worldResponse.getCustomers().forEach(customerResponse -> {
            var customerModel = state.getCustomers()
                    .putIfAbsent(
                            customerResponse.getId(),
                            new CustomerModel().setId(customerResponse.getId())
                    )
                    .setMode(customerResponse.getMode());

            customerResponse.getBasket().forEach(productInBasketResponse -> {
                var product = state.getProducts().get(productInBasketResponse.getId());
                if (null == product) {
                    log.error("No product found for id: {}", customerResponse.getId());
                    return;
                }

                var productInBasketModel = new ProductInBasketModel()
                        .setProduct(product)
                        .setQuantity(productInBasketResponse.getProductCount())
                        .setPrice(productInBasketResponse.getPrie());
                customerModel.addProduct(productInBasketModel);
            });

            presentCustomer.add(customerModel.getId());
        });

        state.getCustomers().forEach((id, customerModel) -> {
            if (!presentCustomer.contains(id)) {
                state.getCustomers().remove(id);
            }
        });
    }

    private void updateEmployees(CurrentWorldResponse worldResponse) {
        var presentEmployees = new HashSet<Integer>();
        worldResponse.getEmployees().forEach(employeeResponse -> {
            var employeeModel = state.getEmployees()
                    .putIfAbsent(
                            employeeResponse.getId(),
                            new EmployeeModel().setId(employeeResponse.getId())
                    )
                    .setFirstName(employeeResponse.getFirstName())
                    .setLastName(employeeResponse.getLastName())
                    .setExperience(employeeResponse.getExperience())
                    .setSalary(employeeResponse.getSalary());

            presentEmployees.add(employeeModel.getId());
        });

        state.getEmployees().forEach((id, employeeModel) -> {
            if (!presentEmployees.contains(id)) {
                state.getEmployees().remove(id);
            }
        });
    }

    private void updateProducts(CurrentWorldResponse worldResponse) {
        var presentProducts = new HashSet<Integer>();
        worldResponse.getStock().forEach(productResponse -> {
            var productModel = state.getProducts()
                    .putIfAbsent(
                            productResponse.getId(),
                            new ProductModel().setId(productResponse.getId())
                    )
                    .setName(productResponse.getName())
                    .setStockPrice(productResponse.getStockPrice())
                    .setQuantity(productResponse.getInStock())
                    .setSellPrice(productResponse.getSellPrice());

            presentProducts.add(productModel.getId());
        });

        state.getProducts().forEach((id, productModel) -> {
            if (!presentProducts.contains(id)) {
                state.getProducts().remove(id);
            }
        });
    }

    private GameStateModel load() {
        awaitServer();

        CurrentWorldResponse worldResponse;
        try {
            worldResponse = api.loadWorld();
        } catch (ApiException e) {
            throw new UnexpectedException("Failed to load game world state", e);
        }

        update(worldResponse);
        stateLoaded = true;

        return state;
    }

    private void awaitServer() {
        var awaitTimes = 120;
        var count = 0;
        var serverReady = false;
        do {
            try {
                count += 1;
                api.loadWorld();
                serverReady = true;

            } catch (ApiException e) {
                try {
                    Thread.currentThread().sleep(1000L);
                } catch (InterruptedException interruptedException) {
                    throw new UnexpectedException("Failed to sleep for waiting for the server to get ready", interruptedException);
                }
            }
        } while (!serverReady && count < awaitTimes);
    }
}
