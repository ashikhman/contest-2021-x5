package com.ashikhman.x5.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class RackCellsModel {

    private Map<Integer, RackCellModel> map = new HashMap<>();

    private List<RackCellModel> list = new ArrayList<>();

    private Map<Integer, List<RackCellModel>> byVisibility = new HashMap<>();
}
