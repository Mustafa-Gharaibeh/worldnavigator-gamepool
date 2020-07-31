package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@ToString
@EqualsAndHashCode
public class AcquirableImpl implements Acquirable {

    @JsonProperty("MaterialMap")
    Map<String, Material> materialMap;

    @JsonCreator
    private AcquirableImpl(@JsonProperty("MaterialMap") Map<String, Material> materialMap) {
        this.materialMap = materialMap;
    }

    public static AcquirableImpl createAcquirable(Map<String, Material> materialMap) {
        return new AcquirableImpl(materialMap);
    }

    public static AcquirableImpl createEmptyAcquirable() {
        return new AcquirableImpl(new HashMap<>());
    }

    @Override
    public Map<String, Material> acquire() {
        return this.materialMap;
    }

    @Override
    public void addItems(List<Item> items) {
        items.forEach(item -> this.materialMap.put(item.name(), item));
    }
}
