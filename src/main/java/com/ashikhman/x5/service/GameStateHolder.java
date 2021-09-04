package com.ashikhman.x5.service;

import com.ashikhman.x5.client.api.ApiException;
import com.ashikhman.x5.client.api.api.PerfectStoreEndpointApi;
import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.exception.UnexpectedException;
import com.ashikhman.x5.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameStateHolder {

    private final PerfectStoreEndpointApi api;

    private boolean stateLoaded = false;

    private GameStateModel state = new GameStateModel();

    @Getter
    private List<GameStateModel> history = new ArrayList<>();

    public GameStateModel getState() {
        if (!stateLoaded) {
            synchronized (this) {
                if (!stateLoaded) {
                    state = loadState();
                }
            }
        }

        return state;
    }

    public void saveHistory() {
        history.add(new GameStateModel(state));
    }

    public GameStateModel insert(CurrentWorldResponse worldResponse) {
        insertCheckoutLines(worldResponse);
        insertRecruitmentOffers(worldResponse);
        insertRackCells(worldResponse);

        return state;
    }

    public GameStateModel update(CurrentWorldResponse worldResponse) {
        state
                .setWorldResponse(worldResponse)
                .setTickCount(worldResponse.getTickCount())
                .setCurrentTick(worldResponse.getCurrentTick())
                .setIncome(worldResponse.getIncome())
                .setSalaryCosts(worldResponse.getSalaryCosts())
                .setStockCosts(worldResponse.getStockCosts())
                .setGameOver(worldResponse.isGameOver())
                .setCommands(new ArrayList<>());

        updateProducts(worldResponse);
        updateEmployees(worldResponse);
        updateCustomer(worldResponse);
        updateCheckoutLines(worldResponse);
        updateRackCells(worldResponse);

        updateEmployeesTime();

        return state;
    }


    private void updateEmployeesTime() {
        for (var entry : state.getEmployees().entrySet()) {
            var employee = entry.getValue();
            if (null == employee.getCheckoutLine()) {
                employee.setVacationTicks(employee.getVacationTicks() + 1);
                employee.setWorkingTicks(1);
            } else {
                employee.setWorkingTicks(employee.getWorkingTicks() + 1);
                employee.setVacationTicks(1);
            }
        }
    }

    private void insertRackCells(CurrentWorldResponse worldResponse) {
        worldResponse.getRackCells().forEach(cellResponse -> {
            var cellModel = new RackCellModel()
                    .setId(cellResponse.getId())
                    .setVisibility(cellResponse.getVisibility())
                    .setCapacity(cellResponse.getCapacity());

            state.getRackCells().getMap().put(cellModel.getId(), cellModel);
            state.getRackCells().getList().add(cellModel);
            state.getRackCells().getByVisibility().computeIfAbsent(cellModel.getVisibility(), k -> new ArrayList<>())
                    .add(cellModel);
        });
    }

    private void updateRackCells(CurrentWorldResponse worldResponse) {
        worldResponse.getRackCells().forEach(cellResponse -> {
            ProductModel product = null;
            if (null != cellResponse.getProductId()) {
                product = state.getProducts().get(cellResponse.getProductId());
                if (null == product) {
                    log.error("No product found for id: {}", cellResponse.getId());
                }
            }

            var cellModel = state.getRackCells().getMap().get(cellResponse.getId());

            var productQuantity = cellResponse.getProductQuantity();
            if (null == productQuantity) {
                productQuantity = 0;
            }

            cellModel
                    .setProduct(product)
                    .setProductName(cellResponse.getProductName())
                    .setProductQuantity(productQuantity)
                    .setProductTime(cellModel.getProductTime() + 1);
        });
    }

    private void insertRecruitmentOffers(CurrentWorldResponse worldResponse) {
        state.getRecruitmentOffers().clear();
        worldResponse.getRecruitmentAgency().forEach(offerResponse -> state.getRecruitmentOffers().add(
                new RecruitmentOfferModel()
                        .setExperience(offerResponse.getExperience())
                        .setEmployeeType(offerResponse.getEmployeeType())
                        .setSalary(offerResponse.getSalary())
        ));

    }

    private void updateCheckoutLines(CurrentWorldResponse worldResponse) {
        state.getEmployees().forEach((integer, employee) -> employee.setCheckoutLine(null));

        worldResponse.getCheckoutLines().forEach(lineResponse -> {
            var lineModel = state.getCheckoutLines().get(lineResponse.getId());

            CustomerModel customer = null;
            if (null != lineResponse.getCustomerId()) {
                customer = state.getCustomers().get(lineResponse.getCustomerId());
                if (null == customer) {
                    log.error("No customer found for id: {}", lineResponse.getCustomerId());
                }
            }
            lineModel.setCustomer(customer);

            EmployeeModel employee = null;
            if (null != lineResponse.getEmployeeId()) {
                employee = state.getEmployees().get(lineResponse.getEmployeeId());
                if (null == employee) {
                    log.error("No employee found for id: {}", lineResponse.getEmployeeId());
                } else {
                    employee.setCheckoutLine(lineModel);
                }
            }
            lineModel.setEmployee(employee);
        });
    }

    private void insertCheckoutLines(CurrentWorldResponse worldResponse) {
        worldResponse.getCheckoutLines().forEach(lineResponse -> {
            var lineModel = new CheckoutLineModel().setId(lineResponse.getId());
            state.getCheckoutLines().put(lineModel.getId(), lineModel);
        });
    }

    private void updateCustomer(CurrentWorldResponse worldResponse) {
        var presentCustomers = new HashSet<Integer>();
        worldResponse.getCustomers().forEach(customerResponse -> {
            var tempCustomerModel = state.getCustomers().get(customerResponse.getId());
            if (null == tempCustomerModel) {
                tempCustomerModel = new CustomerModel().setId(customerResponse.getId());
                state.getCustomers().put(tempCustomerModel.getId(), tempCustomerModel);
            }
            var customerModel = tempCustomerModel;

            customerModel.setMode(customerResponse.getMode());

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

            presentCustomers.add(customerModel.getId());
        });

        state.getCustomers()
                .entrySet()
                .removeIf(entry -> !presentCustomers.contains(entry.getKey()));
    }

    private void updateEmployees(CurrentWorldResponse worldResponse) {
        var presentEmployees = new HashSet<Integer>();
        worldResponse.getEmployees().forEach(employeeResponse -> {
            var employeeModel = state.getEmployees().get(employeeResponse.getId());
            if (null == employeeModel) {
                employeeModel = new EmployeeModel().setId(employeeResponse.getId());
                state.getEmployees().put(employeeModel.getId(), employeeModel);
            }

            employeeModel
                    .setFirstName(employeeResponse.getFirstName())
                    .setLastName(employeeResponse.getLastName())
                    .setExperience(employeeResponse.getExperience())
                    .setSalary(employeeResponse.getSalary());

            presentEmployees.add(employeeModel.getId());
        });

        state.getEmployees()
                .entrySet()
                .removeIf(entry -> !presentEmployees.contains(entry.getKey()));
    }

    private void updateProducts(CurrentWorldResponse worldResponse) {
        var presentProducts = new HashSet<Integer>();
        worldResponse.getStock().forEach(productResponse -> {
            var productModel = state.getProducts().get(productResponse.getId());
            if (null == productModel) {
                productModel = new ProductModel().setId(productResponse.getId());
                state.getProducts().put(productModel.getId(), productModel);
                state.getProductIds().add(productResponse.getId());
            }

            productModel
                    .setName(productResponse.getName())
                    .setStockPrice(productResponse.getStockPrice())
                    .setQuantity(productResponse.getInStock());

            presentProducts.add(productModel.getId());
        });

        state.getProducts()
                .entrySet()
                .removeIf(entry -> {
                    if (!presentProducts.contains(entry.getKey())) {
                        state.getProductIds().remove(entry.getValue().getId());
                        return true;
                    }

                    return false;
                });
    }

    private GameStateModel loadState() {
        var response = loadWord();
        insert(response);
        update(response);
        stateLoaded = true;

        return state;
    }

    public CurrentWorldResponse loadWord() {
        var awaitTimes = 60;
        var count = 0;
        var serverReady = false;
        do {
            try {
                count += 1;

                var worldData = api.loadWorld();
                log.info("World data loaded");

                return worldData;
            } catch (ApiException e) {
                //log.info("Failed to load world data", e);
                try {
                    Thread.currentThread().sleep(1000L);
                } catch (InterruptedException interruptedException) {
                    //interruptedException.printStackTrace();
                }
            }
        } while (!serverReady && count < awaitTimes);

        throw new UnexpectedException(String.format("Game server did not responded in %s seconds", awaitTimes));
    }
}
