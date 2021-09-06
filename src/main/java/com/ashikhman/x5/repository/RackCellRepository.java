package com.ashikhman.x5.repository;

import com.ashikhman.x5.client.api.model.CurrentWorldResponse;
import com.ashikhman.x5.entity.ProductEntity;
import com.ashikhman.x5.entity.RackCellEntity;
import com.ashikhman.x5.exception.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RackCellRepository implements RepositoryInterface {

    private final Map<Integer, RackCellEntity> entities = new HashMap<>();

    private final Map<Integer, List<RackCellEntity>> entitiesGroupedByVisibility = new HashMap<>();

    private final ProductRepository productRepository;

    public RackCellEntity getById(Integer id) {
        var entity = entities.get(id);
        if (null == entity) {
            throw new UnexpectedException(String.format("Rack cell with id `%s` not found", id));
        }

        return entity;
    }

    public Map<Integer, List<RackCellEntity>> getAllGroupedByVisibility() {
        return entitiesGroupedByVisibility;
    }

    public void printAll() {
        var joiner = new StringJoiner("|");
        entities.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> joiner.add(String.format(
                        "%s,%s,%s",
                        entry.getValue().getId(),
                        entry.getValue().getVisibility(),
                        entry.getValue().getCapacity()
                )));

        System.out.printf("CELLS: %s\n", joiner);
    }

    @Override
    public void update(CurrentWorldResponse worldResponse) {
        worldResponse.getRackCells().forEach(cellResponse ->
                getById(cellResponse.getId())
                        .setProductId(cellResponse.getProductId())
                        .setProductName(cellResponse.getProductName())
                        .setProductQuantity(cellResponse.getProductQuantity())
        );
    }

    @Override
    public void init(CurrentWorldResponse worldResponse) {
        worldResponse.getRackCells().forEach(cellResponse -> {
            var entity = new RackCellEntity()
                    .setId(cellResponse.getId())
                    .setCapacity(cellResponse.getCapacity())
                    .setVisibility(cellResponse.getVisibility());

            entities.put(entity.getId(), entity);
            entitiesGroupedByVisibility.computeIfAbsent(entity.getVisibility(), k -> new ArrayList<>())
                    .add(entity);
        });
    }

    @Override
    public void postUpdate() {
        productRepository.getAll().forEach(product -> product.setRackCell(null));

        entities.forEach((id, cell) -> {
            ProductEntity product = null;
            if (null != cell.getProductId()) {
                product = productRepository.getById(cell.getProductId());
            }
            cell.setProduct(product);
        });
    }
}
