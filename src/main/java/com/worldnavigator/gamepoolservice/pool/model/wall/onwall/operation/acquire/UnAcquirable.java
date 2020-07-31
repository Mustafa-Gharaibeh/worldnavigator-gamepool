package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class UnAcquirable implements Acquirable {

    @JsonCreator
    private UnAcquirable() {
    }

    public static UnAcquirable createUnAcquirable() {
        return new UnAcquirable();
    }

    @Override
    public Map<String, Material> acquire() {
        return new HashMap<>();
    }

    @Override
    public void addItems(List<Item> items) {

    }
}
